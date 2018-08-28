package br.com.jonyfs;

import br.com.jonyfs.team.Team;
import br.com.jonyfs.user.User;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.filter.session.SessionFilter;
import java.util.Arrays;
import java.util.HashSet;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class TeamTests extends BasicTests {

    @LocalServerPort
    int port;

    private User admin;

    @Before
    public void init() {
        admin = createUserIfNotFound("admin.team@test.com", createRoleAdminIfNotFound());
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
            .formParam("username", admin.getEmail())
            .formParam("password", admin.getPassword())
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

    @Test
    public void givenAdminUser_whenUserIsLoggedAndAccessCreateNewTeam_thenStatusOK() throws JsonProcessingException {
        try {
            RestAssured.useRelaxedHTTPSValidation();
            SessionFilter sessionFilter = new SessionFilter();

            RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(Include.NON_NULL);
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

            given()
                .port(port)
                .log()
                .all()
                .filter(sessionFilter)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(mapper.writeValueAsString(itTeam))
                .when()
                .post("/teams")
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
