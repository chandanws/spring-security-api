package br.com.jonyfs;

import br.com.jonyfs.privilege.Privilege;
import br.com.jonyfs.role.Role;
import br.com.jonyfs.role.RoleRepository;
import br.com.jonyfs.team.Team;
import br.com.jonyfs.user.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.filter.session.SessionFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import lombok.extern.slf4j.Slf4j;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RoleTests extends BasicTests {

    @LocalServerPort
    int port;

    @Autowired
    RoleRepository roleRepository;

    private User admin;

    @Before
    public void init() {
        admin = createUserIfNotFound("admin.role@test.com", createRoleAdminIfNotFound());
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

    @Test
    public void givenAdminUser_whenUserIsLoggedAndCreateNewRoleAndAssociateASavedUser_thenStatusOK() throws JsonProcessingException {
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

            Privilege priv1 = Privilege
                .builder()
                .name("CAN_SING")
                .build();

            Privilege priv2 = Privilege
                .builder()
                .name("CAN_PLAY_GUITAR")
                .build();

            Role singer = Role
                .builder()
                .name("ROLE_SINGER")
                .privileges(Arrays.asList(priv1, priv2))
                .build();

            Role singerFromRest
                = given()
                    .port(port)
                    .log()
                    .all()
                    .filter(sessionFilter)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(mapper.writeValueAsString(singer))
                    .when()
                    .post("/roles")
                    .then()
                    .log()
                    .all()
                    .statusCode(HttpStatus.CREATED.value())
                    .extract()
                    .response()
                    .as(Role.class);

            assertThat(singerFromRest).isNotNull();

            assertThat(singerFromRest.getId()).isNotNull();

            Privilege priv3 = Privilege
                .builder()
                .name("CAN_DANCE")
                .build();

            singerFromRest.setPrivileges(new ArrayList<>());
            singerFromRest.setUsers(new ArrayList<>());

            singerFromRest.getPrivileges().add(priv3);

            admin.setTeams(new HashSet<>(Arrays.asList(Team.builder().name("Test Team").build())));

            singerFromRest.getUsers().add(admin);

            given()
                .port(port)
                .log()
                .all()
                .filter(sessionFilter)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(mapper.writeValueAsString(singerFromRest))
                .when()
                .put("/roles/" + singerFromRest.getId())
                .then()
                .log()
                .all()
                .statusCode(HttpStatus.OK.value());

            given()
                .port(port)
                .log()
                .all()
                .filter(sessionFilter)
                .get("/roles/{id}/users", singerFromRest.getId())
                .then()
                .log()
                .all()
                .statusCode(HttpStatus.OK.value());

            given()
                .port(port)
                .log()
                .all()
                .filter(sessionFilter)
                .get("/roles/{id}/privileges", singerFromRest.getId())
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
        } catch (Exception e) {
            LOGGER.error("Fail:", e);
            throw e;
        }

    }

}
