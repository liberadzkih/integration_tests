package edu.iis.mto.blog.rest.test;

import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static edu.iis.mto.blog.rest.test.CreateUserTest.USER_API;
import static io.restassured.RestAssured.given;

public class PostCreateTest {
    @Test
    public void shouldAllowCreatingPostByConfirmedUser() {
        JSONObject jsonObject = new JSONObject()
                .put("entry", "DEADBEEF");

        given()
                .accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObject.toString())
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_CREATED)
                .when()
                .post(USER_API + "/1/post");
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 3})
    public void shouldNotAllwoCreatingPostByUnconfirmedUser(int userId) {
        JSONObject jsonObject = new JSONObject()
                .put("entry", "DEADBEEF");

        given()
                .accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObject.toString())
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .when()
                .post(USER_API + "/" + userId + "/post");
    }
}
