package edu.iis.mto.blog.rest.test;

import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class CreatePostTest extends FunctionalTests {

    private static final String USER_API = "/blog/user/";

    @Test
    public void confirmedUserShouldBeAbleToAddPost() {
        String id = "1";
        JSONObject jsonObj = new JSONObject().put("entry", "ExampleData");
        given().accept(ContentType.JSON)
               .header("Content-Type", "application/json;charset=UTF-8")
               .body(jsonObj.toString())
               .expect()
               .log()
               .all()
               .statusCode(HttpStatus.SC_CREATED)
               .when()
               .post(USER_API + id + "/post");
    }

    @Test
    public void newUserShouldNotBeAbleToAddPost() {
        String id = "2";
        JSONObject jsonObj = new JSONObject().put("entry", "ExampleData");
        given().accept(ContentType.JSON)
               .header("Content-Type", "application/json;charset=UTF-8")
               .body(jsonObj.toString())
               .expect()
               .log()
               .all()
               .statusCode(HttpStatus.SC_BAD_REQUEST)
               .when()
               .post(USER_API + id + "/post");
    }
}
