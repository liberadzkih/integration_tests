package edu.iis.mto.blog.rest.test;

import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class SearchBlogPostTest extends FunctionalTests {
    @Test
    public void searchingPostsOfRemovedUserShouldResultWithFailure() {
        JSONObject jsonObj = new JSONObject().put("entry", "xxxxx");
        given().accept(ContentType.JSON)
               .header("Content-Type", "application/json;charset=UTF-8")
               .body(jsonObj.toString())
               .expect()
               .log()
               .all()
               .statusCode(HttpStatus.SC_BAD_REQUEST)
               .when()
               .post("/blog/user/4/post");
    }


}
