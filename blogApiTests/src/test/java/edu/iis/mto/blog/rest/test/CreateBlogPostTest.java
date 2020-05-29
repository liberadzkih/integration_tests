package edu.iis.mto.blog.rest.test;

import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class CreateBlogPostTest  extends FunctionalTests {

//tylko uzytkownik ze statusem CONFIRMED moze dodac post

    private static final String USER_API = "/blog/user/1/post";

    @Test
    public void createBlogPostByConfirmedUserResultWithSuccess() {
        JSONObject jsonObj = new JSONObject().put("entry", "xxxxx");
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
}
