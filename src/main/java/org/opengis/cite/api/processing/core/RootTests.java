package org.opengis.cite.api.processing.core;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.core.StringContains.containsString;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.opengis.cite.api.processing.CommonFixture;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.atlassian.oai.validator.OpenApiInteractionValidator;
import com.atlassian.oai.validator.report.LevelResolver;
import com.atlassian.oai.validator.report.ValidationReport.Level;
import com.atlassian.oai.validator.restassured.OpenApiValidationFilter;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import io.swagger.client.JSON;
import io.swagger.client.model.ComplexData;
import io.swagger.client.model.Execute;
import io.swagger.client.model.Format;
import io.swagger.client.model.Input;
import io.swagger.client.model.Output;
import io.swagger.client.model.TransmissionMode;
import io.swagger.client.model.ValueType;

/**
 * Includes various tests of capability 1.
 */
public class RootTests extends CommonFixture {

    private static final String OPENAPI_SPEC_URL =
            "https://app.swaggerhub.com/apiproxy/schema/file/apis/geoprocessing/WPS-all-in-one/1.0-draft?format=json";

    private static final OpenApiInteractionValidator validator = OpenApiInteractionValidator
            .createFor(OPENAPI_SPEC_URL)
            .withLevelResolver(
                LevelResolver.create().withLevel("validation.schema.additionalProperties", Level.IGNORE).build())
            .build();
    private static final OpenApiValidationFilter validationFilter = new OpenApiValidationFilter(validator);

    private String baseURIValid = "http://localhost:8080/javaps/rest/";

    private Object complexValue;

    private String complexOutputId;

    private String complexInputId;
    private String complexInputMimeType;
//    private String processID2 = "org.n52.javaps.test.EchoProcess3";
//    private String processID3 = "org.n52.javaps.test.EchoProcess4";

    private String complexInputSchema;

    private String complexInputEncoding;

    private String complexOutputMimeType;

    private String complexOutputSchema;

    private String complexOutputEncoding;
    
    @BeforeTest
    public void setup() {
    	
    }

    /**
     * Tests if the root path produces valid JSON.
     */
    @Test
    public void testRootValid() {

        ArrayList<?> links = given(requestSpecificationValid).filter(validationFilter).when().get("/").then().assertThat().statusCode(200).extract().path("links");
                
        Assert.assertTrue(links != null);
        
        for (Object link : links) {
            if(link instanceof Map<?, ?>) {
                Object href = ((Map<?,?>)link).get("href");
                
                get((String) href).then().assertThat().statusCode(200);
                
//                assertTrue(.statusCode() == 200);
//                .then().assertThat().statusCode(200);
//                System.out.println(href);
            }
        }
    }
    
    @Test
    public void testProcessesValid() {

        given(requestSpecificationValid).filter(validationFilter).when().get("processes/").then().assertThat()
                .statusCode(200);
    }

    @Test
    public void testProcessDescriptionValid() {

        given(requestSpecificationValid).filter(validationFilter).when().get("processes/" + processID).then().assertThat()
                .statusCode(200);
    }
    
    //test sync
    //test async (with delay to check the status)
    //test reference output
    
    private String createExecuteJSON() {
        
        complexValue = "<test>value</test>";
        
        complexInputId = "complexInput";
        
        complexInputMimeType = "application/xml";
        
        complexInputSchema = "";
        
        complexInputEncoding = "";
        
        complexOutputId = "complexOutput";
        
        complexOutputMimeType = "application/xml";
        
        complexOutputSchema = "";
        
        complexOutputEncoding = "";
        
        Execute execute = new Execute();
        
        Input input1 = new Input();
        
        ComplexData complexData = new ComplexData();
        
        Format inputFormat = createFormat(complexInputMimeType, complexInputSchema, complexInputEncoding);
        
        complexData.setFormat(inputFormat);
        
        ValueType value = new ValueType();
        
        value.setInlineValue(complexValue);
        
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
    
    private Format createFormat(String mimeType,
            String schema,
            String encoding) {
        
        Format format = new Format();
        
        format.setMimeType(mimeType);
        
        if(schema != null && !schema.isEmpty()) {
            format.setSchema(schema);
        }
        if(encoding != null && !encoding.isEmpty()) {
            format.setEncoding(encoding);
        }
        
        return format;
    }

    @Test
    public void testExecute() throws IOException {
        
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("execute.json");
        
        StringWriter writer = new StringWriter();
        String encoding = StandardCharsets.UTF_8.name();
        IOUtils.copy(inputStream, writer, encoding);
                
        String executeString = createExecuteJSON();
//        String executeString = writer.toString();
        Response response = given(requestSpecificationValid).header("Content-Type", "application/json").body(executeString).when().post("processes/" + processID + "/jobs");
        response.then().assertThat()
        .statusCode(201).header("Location", containsString(baseURIValid + "processes/" + processID + "/jobs/"));
        String location = response.then().extract().header("Location");
        String jobId = location.replace(baseURIValid + "processes/" + processID + "/jobs/", "");
        int i = 0;
        while(i < 3) {
            ResponseBody<?> responseBody = given(requestSpecificationValid).filter(validationFilter).get("processes/" + processID + "/jobs/" + jobId).body();
            Object o = responseBody.path("status");
            if(o instanceof String) {
                String status = (String)o;
                System.out.println(status);
                assertTrue(!status.equals("failed"));
                if(status.equals("successful")) {
                    Object links = responseBody.path("links");
                    if(links instanceof List<?>) {
                        String result = getLink((List<?>) links, "result");
                        System.out.println(result);
                        given(requestSpecificationValid).header("Content-Type", "application/json").filter(validationFilter).get(result.replace(baseURIValid, "")).then().statusCode(200);
                    }
                    break;
                }
            }
            i++;
        }
        
    }
    
    @Test
    public void testExecuteSync() throws IOException {

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("execute.json");

        StringWriter writer = new StringWriter();
        String encoding = StandardCharsets.UTF_8.name();
        IOUtils.copy(inputStream, writer, encoding);

        Response response = given(requestSpecificationValid).header("Content-Type", "application/json")
                .body(writer.toString()).when().post("processes/" + processID + "/jobs?sync-execute=true");
        response.then().assertThat().statusCode(200);
        ResponseBody<?> responseBody = response.body();
        Object o = responseBody.path("outputs");
        assertTrue(o != null);
//        if (o instanceof String) {
//            String status = (String) o;
//            System.out.println(status);
//            assertTrue(!status.equals("failed"));
//            if (status.equals("successful")) {
//                Object links = responseBody.path("links");
//                if (links instanceof List<?>) {
//                    String result = getLink((List<?>) links, "result");
//                    System.out.println(result);
//                    given(requestSpecificationValid).header("Content-Type", "application/json").filter(validationFilter);
//                }
//            }
//        }

    }
    
    @Test
    public void testJobListValid() {

    	//TODO issue execute request first to have at least one job id? 
    	
        given(requestSpecificationValid).filter(validationFilter).when().get("processes/" + processID + "/jobs/").then().assertThat()
                .statusCode(200);
    }
    
    private String getLink(List<?> links, String relName) {
        
        for (Object link : links) {
            if(link instanceof Map<?, ?>) {
                Object rel = ((Map<?,?>)link).get("rel");
                Object href = ((Map<?,?>)link).get("href");
                if(rel.equals(relName)) {
                    return (String) href;
                }
                
            }
        }
        return "";
    }
    
    @Test
    public void testProcessExecutionValid() {
        
        given(requestSpecificationValid).filter(validationFilter).when().get("processes/").then().assertThat()
        .statusCode(200);
    }
}
