package edu.iis.mto.blog.rest.test;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class CreatePostTest extends FunctionalTests {

    private static final String USER_API = "/blog/user/id/post";
    private static final String EMAIL = "pawel2@domain.com";

    @Test
    public void onlyConfirmedUserCanAddPost() {
        Response response = addUser(EMAIL);
        Long id = getIdFromResponse(response);
        JSONObject jsonObj = new JSONObject().put("entry", "tresc posta pierwszego");

        given().accept(ContentType.JSON)
               .header("Content-Type", "application/json;charset=UTF-8")
               .body(jsonObj.toString())
               .expect()
               .log()
               .all()
               .statusCode(HttpStatus.SC_CREATED)
               .when()
               .post(USER_API.replace("id", id.toString()));
    }
}
