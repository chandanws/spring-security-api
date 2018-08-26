package br.com.jonyfs;

import br.com.jonyfs.user.User;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.filter.session.SessionFilter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class TeamTests extends BasicTests {

    @LocalServerPort
    int port;

    private User user;

    @Before
    public void init() {
        user = createUserIfNotFound("admin.team@test.com", createRoleAdminIfNotFound());
    }

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
            .formParam("username", user.getEmail())
            .formParam("password", user.getPassword())
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
            .get("/teams")
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
