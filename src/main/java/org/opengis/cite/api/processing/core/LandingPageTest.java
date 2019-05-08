package org.opengis.cite.api.processing.core;

import static io.restassured.RestAssured.given;

import java.util.ArrayList;

import org.opengis.cite.api.processing.CommonFixture;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * Includes various tests of capability 1.
 */
public class LandingPageTest extends CommonFixture {
	
	private static final String ROOT_PATH = "/";
	private static final String API_PATH = ROOT_PATH + "api";
	private static final String LINKS = "links";
	private static final String SERVICE = "service";
	private static final String CONFORMANCE = "conformance";
	private static final String PROCESSES = "processes";
	private static final String CONFORMS_TO = "conformsTo";
	private static final String CONFORMACE_CLASSES_EMPTY = "No conformance classes present.";
	private static final String CONFORMANCE_PATH = ROOT_PATH + CONFORMANCE;

	// private Object complexValue;
	@BeforeTest
	public void setup() {

	}

	/**
	 * Validate that a landing page can be retrieved from the expected location.
	 * 
	 * @see "OGC 18-062, A.4.2.1"
	 */
	@Test
	public void testLandingPageRetrieval() {
		// just test if landing page returns HTTP 200
		given(requestSpecificationValid).when().get(ROOT_PATH).then().assertThat().statusCode(200);
	}

	/**
	 * Validate that the landing page complies with the require structure and
	 * contents.
	 * 
	 * @see "OGC 18-062, A.4.2.2"
	 */
	@Test
	public void testLandingPageContent() {

		// test against root.yaml, extract links
		ArrayList<?> links = given(requestSpecificationValid).filter(validationFilter).when().get(ROOT_PATH).then()
				.assertThat().statusCode(200).extract().path(LINKS);

		Assert.assertTrue(links != null);

		getLink(links, SERVICE);
		getLink(links, CONFORMANCE);
		getLink(links, PROCESSES);
	}

	/**
	 * Validate that the API Definition document can be retrieved from the expected
	 * location.
	 * 
	 * @see "OGC 18-062, A.4.2.3"
	 */
	@Test
	public void testOpenAPIDocumentRetrieval() {

		// test /api path
		given(requestSpecificationValid).when().get(API_PATH).then()
				.assertThat().statusCode(200);

//		Assert.assertTrue(links != null);
//		
//		Object apiLinkObject = getLink(links, SERVICE);
//		
//		if (apiLinkObject instanceof Map<?, ?>) {
//			Object href = ((Map<?, ?>) apiLinkObject).get(HREF);
//
//			get((String) href).then().assertThat().statusCode(200);
//		}
	}

	/**
	 * Validate that a landing page can be retrieved from the expected location.
	 * 
	 * @see "OGC 18-062, A.4.2.1"
	 */
	@Test
	public void testConformanceRetrieval() {
		// just test if landing page returns HTTP 200
		given(requestSpecificationValid).when().get(ROOT_PATH).then().assertThat().statusCode(200);
	}

	/**
	 * Validate that the landing page complies with the require structure and
	 * contents.
	 * 
	 * @see "OGC 18-062, A.4.2.2"
	 */
	@Test
	public void testConformanceContent() {

		// test against root.yaml, extract links
		ArrayList<?> conformanceClasses = given(requestSpecificationValid).filter(validationFilter).when().get(CONFORMANCE_PATH).then()
				.assertThat().statusCode(200).extract().path(CONFORMS_TO);
		
		Assert.assertNotNull(conformanceClasses, CONFORMACE_CLASSES_EMPTY);
		
		//TODO save conformance classes in suite for testing
	}
}
