package edu.iis.mto.blog.rest.test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

import groovy.transform.ToString;
import io.restassured.path.json.JsonPath;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import io.restassured.http.ContentType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.annotation.Target;
import java.util.List;
import java.util.stream.Collectors;

public class CreateUserTest extends FunctionalTests {

    public static final String USER_API = "/blog/user";

    @Test
    public void createUserWithProperDataReturnsCreatedStatus() {
        JSONObject jsonObj = new JSONObject().put("email", "tracy1@domain.com");
        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj.toString())
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_CREATED)
                .when()
                .post(USER_API);
    }

    @Test
    public void shouldNotAllowUserCreationWithOccupiedEmail() {
        JSONObject jsonObject = new JSONObject()
                .put("email", "215920@edu.p.lodz.pl")
                .put("firstName", "Dawid")
                .put("lastName", "Witaszek");

        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObject.toString())
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_CONFLICT)
                .when()
                .post(USER_API);
    }
}
