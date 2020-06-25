package edu.iis.mto.blog.rest.test;

import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class AddBlogPostTest extends FunctionalTests {
    private static final Long CONFIRMED_USER_ID = 1L;
    private static final Long NOT_CONFIRMED_USER_ID = 2L;
    private static final String USER_API = "/blog/user/{USER_ID}/post";

    @Test
    public void confirmedUserShouldBeAbleToCreatePost() {
        JSONObject jsonObject = new JSONObject().put("entry", "Irrelevant title");
        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObject.toString())
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_CREATED)
                .when()
                .post(getUserApiForUserId(CONFIRMED_USER_ID));
    }

    @Test
    public void notConfirmedUserShouldNotBeAbleToCreatePost() {
        JSONObject jsonObject = new JSONObject().put("entry", "Irrelevant title");
        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObject.toString())
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .when()
                .post(getUserApiForUserId(NOT_CONFIRMED_USER_ID));
    }

    private String getUserApiForUserId(Long userId) {
        return USER_API.replace("{USER_ID}", userId.toString());
    }
}
