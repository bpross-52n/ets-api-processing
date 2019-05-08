package org.opengis.cite.api.processing.core;

import static io.restassured.RestAssured.given;

import org.opengis.cite.api.processing.CommonFixture;
import org.testng.annotations.Test;

public class GetProcessesTest extends CommonFixture {
    
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

}
