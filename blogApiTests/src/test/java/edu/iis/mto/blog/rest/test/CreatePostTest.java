package edu.iis.mto.blog.rest.test;

import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class CreatePostTest extends FunctionalTests {

    private static final String USER_POST_API = "/blog/user/{id}/post";

    @Test
    public void postShouldBeAddedWhenUserStatusIsConfirmed() {
        JSONObject jsonObj = new JSONObject().put("entry", "ENTRY_POST");
        given().accept(ContentType.JSON)
               .header("Content-Type", "application/json;charset=UTF-8")
               .body(jsonObj.toString())
               .expect()
               .log()
               .all()
               .statusCode(HttpStatus.SC_CREATED)
               .when()
               .post(USER_POST_API, "1");
    }

    @Test
    public void postShouldNotBeAddedWhenUserStatusIsNotConfirmed() {
        JSONObject jsonObj = new JSONObject().put("entry", "ENTRY_POST");
        given().accept(ContentType.JSON)
               .header("Content-Type", "application/json;charset=UTF-8")
               .body(jsonObj.toString())
               .expect()
               .log()
               .all()
               .statusCode(HttpStatus.SC_BAD_REQUEST)
               .when()
               .post(USER_POST_API, "2");
    }

}
