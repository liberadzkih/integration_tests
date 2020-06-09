package edu.iis.mto.blog.rest.test;

import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class LikePostTest extends FunctionalTests {

    @Test
    public void likingBlogPostByNotConfirmedUserResultWithBadRequst() {
        JSONObject jsonObj = new JSONObject().put("entry", "xxxxx");
        given().accept(ContentType.JSON)
               .header("Content-Type", "application/json;charset=UTF-8")
               .body(jsonObj.toString())
               .expect()
               .log()
               .all()
               .statusCode(HttpStatus.SC_BAD_REQUEST)
               .when()
               .post("/blog/user/2/like/1");
    }

    @Test
    public void likingBlogPostByConfirmedUserResultWithSuccess() {
        JSONObject jsonObj = new JSONObject().put("entry", "xxxxx");
        given().accept(ContentType.JSON)
               .header("Content-Type", "application/json;charset=UTF-8")
               .body(jsonObj.toString())
               .expect()
               .log()
               .all()
               .statusCode(HttpStatus.SC_OK)
               .when()
               .post("/blog/user/1/like/1");
    }

    @Test
    public void likingBlogPostByOwnerResultBadRequest() {
        JSONObject jsonObj = new JSONObject().put("entry", "xxxxx");
        given().accept(ContentType.JSON)
               .header("Content-Type", "application/json;charset=UTF-8")
               .body(jsonObj.toString())
               .expect()
               .log()
               .all()
               .statusCode(HttpStatus.SC_BAD_REQUEST)
               .when()
               .post("/blog/user/3/like/1");
    }

}
