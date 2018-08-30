package br.com.jonyfs;

import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class SwaggerTests extends BasicTests {

    @LocalServerPort
    int port;

    @Test
    public void givenSwaggerConfigOK_whenUserAccessResourcePage_thenStatusOK() {

        RestAssured.useRelaxedHTTPSValidation();

        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        given()
            .port(port)
            .log()
            .all()
            .when()
            .get("/swagger-ui.html")
            .then()
            .log()
            .all()
            .statusCode(HttpStatus.OK.value());

    }
}
