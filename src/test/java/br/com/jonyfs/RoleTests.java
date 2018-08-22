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
public class RoleTests extends BasicTests {

    @LocalServerPort
    int port;

    @Test
    public void givenUserAndPassword_whenUserInfoIsOkAndAccessResourcePage_thenStatusOK() {

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
            .get("/roles")
            .then()
            .log()
            .all()
            .statusCode(HttpStatus.OK.value());

        given()
            .port(port)
            .log()
            .all()
            .filter(sessionFilter)
            .when()
            .post("/logout")
            .then()
            .log()
            .all()
            .statusCode(HttpStatus.MOVED_TEMPORARILY.value());

    }

}
