package edu.iis.mto.blog.rest.test;

import static io.restassured.RestAssured.given;

import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import io.restassured.http.ContentType;

public class CreatingPostTest extends FunctionalTests {

    private static final String USER_API = "/blog/user/{id}/post";

    @Test
    public void notConfirmedUserCreatingPostShouldReturnBadRequest() {
        JSONObject jsonObj = new JSONObject().put("entry", "Entry");
        given().accept(ContentType.JSON)
               .header("Content-Type", "application/json;charset=UTF-8")
               .body(jsonObj.toString())
               .expect()
               .log()
               .all()
               .statusCode(HttpStatus.SC_BAD_REQUEST)
               .when()
               .post(USER_API, 2);
    }

    @Test
    public void confirmedUserCreatingPostShouldResultInSuccess() {
        JSONObject jsonObj = new JSONObject().put("entry", "Entry");
        given().accept(ContentType.JSON)
               .header("Content-Type", "application/json;charset=UTF-8")
               .body(jsonObj.toString())
               .expect()
               .log()
               .all()
               .statusCode(HttpStatus.SC_CREATED)
               .when()
               .post(USER_API, 1);
    }
}
