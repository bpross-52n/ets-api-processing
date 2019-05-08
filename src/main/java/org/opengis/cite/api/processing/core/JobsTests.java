package org.opengis.cite.api.processing.core;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.StringContains.containsString;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.opengis.cite.api.processing.CommonFixture;
import org.testng.annotations.Test;

import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.swagger.client.model.ComplexData;
import io.swagger.client.model.Execute;
import io.swagger.client.model.Format;
import io.swagger.client.model.Input;
import io.swagger.client.model.Output;
import io.swagger.client.model.TransmissionMode;
import io.swagger.client.model.ValueType;

public class JobsTests extends CommonFixture {

	// test sync
	// test async (with delay to check the status)
	// test reference output

	private String createExecuteJSON() {

		Execute execute = new Execute();

		Input input1 = new Input();

		ComplexData complexData = new ComplexData();

		Format inputFormat = createFormat(complexInputMimeType, complexInputSchema, complexInputEncoding);

		complexData.setFormat(inputFormat);

		ValueType value = new ValueType();

		value.setInlineValue(complexInputValue);

		complexData.setValue(value);

		input1.setInput(complexData);

		input1.setId(complexInputId);

		execute.addInputsItem(input1);

		Output outputsItem = new Output();

		outputsItem.setId(complexOutputId);

		Format outputFormat = createFormat(complexOutputMimeType, complexOutputSchema, complexOutputEncoding);

		outputsItem.setFormat(outputFormat);

		outputsItem.setTransmissionMode(TransmissionMode.VALUE);

		execute.addOutputsItem(outputsItem);

		return json.serialize(execute);
	}

	private Format createFormat(String mimeType, String schema, String encoding) {

		Format format = new Format();

		format.setMimeType(mimeType);

		if (schema != null && !schema.isEmpty()) {
			format.setSchema(schema);
		}
		if (encoding != null && !encoding.isEmpty()) {
			format.setEncoding(encoding);
		}

		return format;
	}

	@Test
	public void testExecute() throws IOException {

		String executeString = createExecuteJSON();
		Response response = given(requestSpecificationValid).header("Content-Type", "application/json")
				.body(executeString).when().post("processes/" + processID + "/jobs");
		response.then().assertThat().statusCode(201).header("Location",
				containsString(baseURIValid + "processes/" + processID + "/jobs/"));
		String location = response.then().extract().header("Location");
		String jobId = location.replace(baseURIValid + "processes/" + processID + "/jobs/", "");
		int i = 0;
		while (i < 3) {
			ResponseBody<?> responseBody = given(requestSpecificationValid).filter(validationFilter)
					.get("processes/" + processID + "/jobs/" + jobId).body();
			Object o = responseBody.path("status");
			if (o instanceof String) {
				String status = (String) o;
				System.out.println(status);
				assertTrue(!status.equals("failed"));
				if (status.equals("successful")) {
					Object links = responseBody.path("links");
					if (links instanceof List<?>) {
						String result = "";
						Object resultLinkObject = getLink((List<?>) links, "result");
						if (resultLinkObject instanceof Map<?, ?>) {
							result = (String) ((Map<?, ?>) resultLinkObject).get(HREF);
						}
						System.out.println(result);
						given(requestSpecificationValid).header("Content-Type", "application/json")
								.filter(validationFilter).get(result.replace(baseURIValid, "")).then().statusCode(200);
					}
					break;
				}
			}
			i++;
		}

	}

	@Test
	public void testExecuteSync() throws IOException {
		String executeString = createExecuteJSON();
		Response response = given(requestSpecificationValid).header("Content-Type", "application/json")
				.body(executeString).when().post("processes/" + processID + "/jobs?sync-execute=true");
		response.then().assertThat().statusCode(200);
		ResponseBody<?> responseBody = response.body();
		Object o = responseBody.path("outputs");
		assertTrue(o != null);
		// if (o instanceof String) {
		// String status = (String) o;
		// System.out.println(status);
		// assertTrue(!status.equals("failed"));
		// if (status.equals("successful")) {
		// Object links = responseBody.path("links");
		// if (links instanceof List<?>) {
		// String result = getLink((List<?>) links, "result");
		// System.out.println(result);
		// given(requestSpecificationValid).header("Content-Type",
		// "application/json").filter(validationFilter);
		// }
		// }
		// }

	}

	@Test
	public void testJobListValid() {

		// TODO issue execute request first to have at least one job id?

		given(requestSpecificationValid).filter(validationFilter).when().get("processes/" + processID + "/jobs/").then()
				.assertThat().statusCode(200);
	}

}
