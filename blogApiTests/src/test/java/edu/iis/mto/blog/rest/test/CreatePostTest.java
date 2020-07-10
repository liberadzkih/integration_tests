package edu.iis.mto.blog.rest.test;

import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class CreatePostTest extends FunctionalTests {

    private static final String USER_API = "/blog/user/";

    @Test
    public void addPostByConfirmedUserReturnsCreatedStatus() {
        String confirmedUserId = "1";
        JSONObject jsonObj = new JSONObject().put("entry", "some post");
        given().accept(ContentType.JSON)
               .header("Content-Type", "application/json;charset=UTF-8")
               .body(jsonObj.toString())
               .expect()
               .log()
               .all()
               .statusCode(HttpStatus.SC_CREATED)
               .when()
               .post(USER_API + confirmedUserId + "/post");
    }

    @Test
    public void addPostByNotConfirmedUserReturnsbadRequestStatus() {
        String notConfirmedUserId = "2";
        JSONObject jsonObj = new JSONObject().put("entry", "some post");
        given().accept(ContentType.JSON)
               .header("Content-Type", "application/json;charset=UTF-8")
               .body(jsonObj.toString())
               .expect()
               .log()
               .all()
               .statusCode(HttpStatus.SC_BAD_REQUEST)
               .when()
               .post(USER_API + notConfirmedUserId + "/post");
    }
}
