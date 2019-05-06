package org.opengis.cite.api.processing;

import javax.xml.parsers.DocumentBuilder;

import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.w3c.dom.Document;

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
    	
        RequestSpecBuilder builder = new RequestSpecBuilder();

        builder.setBaseUri(obj.toString());

        requestSpecificationValid = builder.build();
        
    }

    @BeforeMethod
    public void clearMessages() {
        this.request = null;
        this.response = null;
    }

}
