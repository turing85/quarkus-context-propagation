package de.turing85;

import de.turing85.quarkus.context.propagation.AddValueToMdcFilter;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class MdcEndpointTest {

    @Test
    public void testHelloEndpoint() {
        given()
          .when().get(AddValueToMdcFilter.MDC_KEY)
          .then()
             .statusCode(200)
             .body(is(AddValueToMdcFilter.MDC_VALUE));
    }

}