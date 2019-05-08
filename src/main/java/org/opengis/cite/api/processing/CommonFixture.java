package org.opengis.cite.api.processing;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;

import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.w3c.dom.Document;

import com.atlassian.oai.validator.OpenApiInteractionValidator;
import com.atlassian.oai.validator.report.LevelResolver;
import com.atlassian.oai.validator.report.ValidationReport.Level;
import com.atlassian.oai.validator.restassured.OpenApiValidationFilter;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientResponse;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.swagger.client.JSON;

/**
 * A supporting base class that sets up a common test fixture. These
 * configuration methods are invoked before those defined in a subclass.
 */
public class CommonFixture {

    /**
     * Root test suite package (absolute path).
     */
    public static final String ROOT_PKG_PATH = "/org/opengis/cite/api.processing/";
    /**
     * HTTP client component (JAX-RS Client API).
     */
    protected Client client;
    /**
     * An HTTP request message.
     */
    protected ClientRequest request;
    /**
     * An HTTP response message.
     */
    protected ClientResponse response;
    
    protected Object obj;
    
    /** A DOM parser. */
    protected DocumentBuilder docBuilder;
    protected static final String TNS_PREFIX = "tns";
    /** A Document representing the content of the request message. */
    protected Document reqEntity;
    /** A Document representing the content of the response message. */
    protected Document rspEntity;
    
    protected RequestSpecification requestSpecificationValid;
    
    protected String processID;

    protected JSON json;
    
    protected boolean testComplexInput;
    
    protected String complexInputId;
    
    protected String complexInputValue;
    
    protected String complexInputMimeType;
    
    protected String complexInputSchema;
    
    protected String complexInputEncoding;
    
    protected boolean testComplexOutput;

    protected String complexOutputId;

    protected String complexOutputMimeType;

    protected String complexOutputSchema;

    protected String complexOutputEncoding;    

    protected static final String OPENAPI_SPEC_URL =
            "https://app.swaggerhub.com/apiproxy/schema/file/apis/geoprocessing/WPS-all-in-one/1.0-draft?format=json";

    protected static final OpenApiInteractionValidator validator = OpenApiInteractionValidator
            .createFor(OPENAPI_SPEC_URL)
            .withLevelResolver(
                LevelResolver.create().withLevel("validation.schema.additionalProperties", Level.IGNORE).build())
            .build();
    protected static final OpenApiValidationFilter validationFilter = new OpenApiValidationFilter(validator);

    protected String baseURIValid = "http://localhost:8080/javaps/rest/";
    
	private String processesPath = "processes/";

	protected static final String REL = "rel";
	protected static final String REL_NOT_STRING = "Rel not of type String";
	protected static final String LINK_NOT_CONTAINED = "Landing Page does not contain link: '%s'.";
	protected static final String HREF = "href";

    /**
     * Initializes the common test fixture with a client component for 
     * interacting with HTTP endpoints.
     *
     * @param testContext The test context that contains all the information for
     * a test run, including suite attributes.
     */
    @BeforeClass(alwaysRun = true)
    public void initCommonFixture(ITestContext testContext) {
        
        json = new JSON();

    	processID = (String) testContext.getSuite().getAttribute(SuiteAttribute.PROCESSID.getName());
    	
        obj = testContext.getSuite().getAttribute(SuiteAttribute.TEST_SUBJECT.getName());
        
        testComplexInput = (boolean) testContext.getSuite().getAttribute(SuiteAttribute.TESTCOMPLEXINPUT.getName());
    	
        complexInputId = (String) testContext.getSuite().getAttribute(SuiteAttribute.COMPLEXINPUTID.getName());
        
        complexInputValue = (String) testContext.getSuite().getAttribute(SuiteAttribute.COMPLEXINPUTVALUE.getName());
        
        complexInputMimeType = (String) testContext.getSuite().getAttribute(SuiteAttribute.COMPLEXINPUTMIMETYPE.getName());
        
        complexInputSchema = (String) testContext.getSuite().getAttribute(SuiteAttribute.COMPLEXINPUTSCHEMA.getName());
        
        complexInputEncoding = (String) testContext.getSuite().getAttribute(SuiteAttribute.COMPLEXINPUTENCODING.getName());		
		
		testComplexOutput = (boolean) testContext.getSuite().getAttribute(SuiteAttribute.TESTCOMPLEXOUTPUT.getName());
    	
        complexOutputId = (String) testContext.getSuite().getAttribute(SuiteAttribute.COMPLEXOUTPUTID.getName());
        
        complexOutputMimeType = (String) testContext.getSuite().getAttribute(SuiteAttribute.COMPLEXOUTPUTMIMETYPE.getName());
        
        complexOutputSchema = (String) testContext.getSuite().getAttribute(SuiteAttribute.COMPLEXOUTPUTSCHEMA.getName());
        
        complexOutputEncoding = (String) testContext.getSuite().getAttribute(SuiteAttribute.COMPLEXOUTPUTENCODING.getName());
        
        RequestSpecBuilder builder = new RequestSpecBuilder();

        builder.setBaseUri(obj.toString());

        requestSpecificationValid = builder.build();
        
    }

    @BeforeMethod
    public void clearMessages() {
        this.request = null;
        this.response = null;
    }
    
    protected String getProcessesPath() {
    	return processesPath ;
    }
    
    protected String getProcessDescriptionPath(String processId) {
    	return getProcessesPath() + processId;
    }
    
    protected String getJobsPath(String processId) {
    	return getProcessesPath() + processId + "/jobs";
    }
    
    protected String getJobStatusPath(String processId, String jobId) {
    	return getJobsPath(processId) + "/" + jobId;
    }

    protected Object getLink(List<?> links, String relName) {

		Object resultLink = null;
		
		for (Object link : links) {
			if (link instanceof Map<?, ?>) {
				Object relObject = ((Map<?, ?>) link).get(REL);
				Assert.assertTrue(relObject instanceof String, REL_NOT_STRING);
				if (relName.equals(relName)) {
					resultLink = link;
				}
			}
		}
		Assert.assertNotNull(resultLink, String.format(LINK_NOT_CONTAINED, relName));
		return resultLink;
	}

}
