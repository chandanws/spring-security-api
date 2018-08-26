package br.com.jonyfs;

import br.com.jonyfs.user.User;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.filter.session.SessionFilter;
import lombok.extern.slf4j.Slf4j;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class UserTests extends BasicTests {

    @LocalServerPort
    int port;

    @Test
    public void givenUserAndPassword_whenUserInfoIsOkAndAccessResourcePage_thenStatusOK() {

        RestAssured.useRelaxedHTTPSValidation();
        SessionFilter sessionFilter = new SessionFilter();

        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        User user = createUserIfNotFound("admin3@test.com", createRoleAdminIfNotFound());

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
            .get("/users")
            .then()
            .log()
            .all()
            .statusCode(HttpStatus.OK.value());

        given()
            .port(port)
            .log()
            .all()
            .filter(sessionFilter)
            .get("/users/{id}", user.getId())
            .then()
            .log()
            .all()
            .statusCode(HttpStatus.OK.value())
            .body("email", equalTo(user.getEmail()));

        User userFromRest = given()
            .port(port)
            .log()
            .all()
            .filter(sessionFilter)
            .get("/users/{id}", user.getId()).as(User.class);

        LOGGER.info("User parsed from Rest:{}", userFromRest);

        assertThat(userFromRest).isNotNull();

        assertThat(userFromRest.getEmail()).isNotNull();

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
