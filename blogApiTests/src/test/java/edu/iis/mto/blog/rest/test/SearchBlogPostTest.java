package edu.iis.mto.blog.rest.test;

import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItems;

public class SearchBlogPostTest extends FunctionalTests {
    @Test
    public void searchingPostsOfRemovedUserShouldResultWithFailure() {
        given().accept(ContentType.JSON)
               .header("Content-Type", "application/json;charset=UTF-8")
               .expect()
               .log()
               .all()
               .statusCode(HttpStatus.SC_BAD_REQUEST)
               .when()
               .post("/blog/user/4/post");
    }
    @Test
    public void searchingPostsOfNotRemovedUserShouldResultWithSuccessAndProperCountOfLikes() {
        given().accept(ContentType.JSON)
               .header("Content-Type", "application/json;charset=UTF-8")
               .expect()
               .log()
               .all()
               .statusCode(HttpStatus.SC_OK)
               .body("likesCount",hasItems(1))
               .when()
               .get("/blog/post/1");
    }

}
