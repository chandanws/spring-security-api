package br.com.jonyfs;

import br.com.jonyfs.team.Team;
import br.com.jonyfs.user.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.filter.session.SessionFilter;
import java.util.Arrays;
import java.util.HashSet;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class UserTests extends BasicTests {

    @LocalServerPort
    int port;

    private User admin;

    @Before
    public void init() {
        admin = createUserIfNotFound("admin.user@test.com", createRoleAdminIfNotFound());
    }


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

    @Transactional
    @Test
    public void givenAdminUser_whenUserIsLoggedAndCreateNewUser_thenStatusOK() throws JsonProcessingException {
        try {
            RestAssured.useRelaxedHTTPSValidation();
            SessionFilter sessionFilter = new SessionFilter();

            RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            given()
                .port(port)
                .log()
                .all()
                .filter(sessionFilter)
                .formParam("username", admin.getEmail())
                .formParam("password", admin.getPassword())
                .when()
                .post("/login")
                .then()
                .log()
                .all()
                .statusCode(HttpStatus.OK.value());

            Team devTeam = Team
                .builder()
                .name("Dev Team")
                .build();
            Team qaTeam = Team
                .builder()
                .name("QA Team")
                .build();
            Team managersTeam = Team
                .builder()
                .name("Managers Team")
                .build();

            Team itTeam = Team
                .builder()
                .name("IT Team")
                .children(new HashSet<>(Arrays.asList(devTeam, qaTeam, managersTeam)))
                .build();

            User user = User
                .builder()
                .firstName("newuser")
                .lastName("newuser")
                .password("password")
                .email("newuserFromAdmin@test.com")
                .teams(new HashSet<>(Arrays.asList(itTeam)))
                .roles(Arrays.asList(createRoleAdminIfNotFound()))
                .build();

            given()
                .port(port)
                .log()
                .all()
                .filter(sessionFilter)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(mapper.writeValueAsString(user))
                .when()
                .post("/users")
                .then()
                .log()
                .all()
                .statusCode(HttpStatus.CREATED.value());


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
        } catch (Exception e) {
            LOGGER.error("Fail:", e);
        }

    }

}
