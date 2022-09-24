package de.turing85;

import de.turing85.quarkus.context.propagation.MdcEndpoint;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@QuarkusTest
@TestHTTPEndpoint(MdcEndpoint.class)
class MdcEndpointTest {

  @ConfigProperty(name = "app.config.http.header.keys.correlation-id")
  String correlationIdHeader;

  @Test
  void testHMdcEndpointWithExplicitCorrelationId() {
    final String correlationId = "some-correlation-id";
    RestAssured
        .given()
            .header(correlationIdHeader, correlationId)
        .when()
            .get()
        .then()
            .statusCode(200)
            .header(correlationIdHeader, is(correlationId))
            .body(is(correlationId));
  }

  @Test
  void testHMdcEndpointWithGeneratedCorrelationId() {
    final Response response = RestAssured.given()
        .when()
        .get()
        .then()
        .statusCode(200)
        .extract().response();

    final String correlationIdFromHeader = response.header(Objects.requireNonNull(correlationIdHeader));
    assertThat(correlationIdFromHeader, is(notNullValue()));
    assertThat(correlationIdFromHeader.isBlank(), is(false));

    final String correlationIdFromBody = response.getBody().asString();
    assertThat(correlationIdFromBody, is(notNullValue()));
    assertThat(correlationIdFromBody.isBlank(), is(false));

    assertThat(correlationIdFromHeader, is(correlationIdFromBody));
  }
}