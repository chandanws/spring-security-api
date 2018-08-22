package br.com.jonyfs;

import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.filter.session.SessionFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginTests extends BasicTests {

    @LocalServerPort
    int port;

    @Test
    public void givenUserAndPassword_whenUserInfoIsOk_thenStatusOK() {

        RestAssured.useRelaxedHTTPSValidation();
        SessionFilter sessionFilter = new SessionFilter();

        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        given()
            .port(port)
            .log()
            .all()
            .filter(sessionFilter)
            .formParam("username", "admin@test.com")
            .formParam("password", "password")
            .when()
            .post("/login")
            .then()
            .log()
            .all()
            .statusCode(HttpStatus.OK.value());

        given()
            .port(port)
            .log()
            .all()
            .filter(sessionFilter)
            .get("/me")
            .then()
            .log()
            .all()
            .statusCode(HttpStatus.OK.value());

    }

    @Test
    public void givenUserAndPassword_whenUserIsUnknow_thenStatusUnauthorized() {

        RestAssured.useRelaxedHTTPSValidation();
        SessionFilter sessionFilter = new SessionFilter();

        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        given()
            .port(port)
            .log()
            .all()
            .filter(sessionFilter)
            .formParam("username", "unknow@test.com")
            .formParam("password", "password")
            .when()
            .post("/login")
            .then()
            .log()
            .all()
            .statusCode(HttpStatus.UNAUTHORIZED.value());

    }
}
