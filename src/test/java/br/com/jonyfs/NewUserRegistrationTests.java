package br.com.jonyfs;

import br.com.jonyfs.user.UserDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
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
public class NewUserRegistrationTests {

    @LocalServerPort
    int port;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void givenNewUserRequest_whenUserDtoIsValid_thenStatusOK() throws JsonProcessingException {

        RestAssured.useRelaxedHTTPSValidation();
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        UserDto user = UserDto
            .builder()
            .firstName("newuser")
            .lastName("newuser")
            .password("password")
            .email("newuser@test.com")
            .build();

        given()
            .port(port)
            .log()
            .all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(objectMapper.writeValueAsString(user))
            .when()
            .post("/newUser")
            .then()
            .log()
            .all()
            .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    public void givenNewUserRequest_whenUserDtoEmailAlredyExists_thenStatusBadRequest() throws JsonProcessingException {

        RestAssured.useRelaxedHTTPSValidation();
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        UserDto user = UserDto
            .builder()
            .firstName("newuser2")
            .lastName("newuser2")
            .password("password")
            .email("newuser2@test.com")
            .build();

        given()
            .port(port)
            .log()
            .all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(objectMapper.writeValueAsString(user))
            .when()
            .post("/newUser")
            .then()
            .log()
            .all()
            .statusCode(HttpStatus.CREATED.value());

        given()
            .port(port)
            .log()
            .all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(objectMapper.writeValueAsString(user))
            .when()
            .post("/newUser")
            .then()
            .log()
            .all()
            .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void givenNewUserRequest_whenUserDtoInvalidEmail_thenStatusBadRequest() throws JsonProcessingException {

        RestAssured.useRelaxedHTTPSValidation();
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        UserDto user = UserDto
            .builder()
            .firstName("newuser2")
            .lastName("newuser2")
            .password("password")
            .email("wrongemail.test.com")
            .build();

        given()
            .port(port)
            .log()
            .all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(objectMapper.writeValueAsString(user))
            .when()
            .post("/newUser")
            .then()
            .log()
            .all()
            .statusCode(HttpStatus.BAD_REQUEST.value());

    }

}
