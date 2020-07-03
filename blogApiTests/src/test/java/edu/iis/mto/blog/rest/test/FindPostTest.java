package edu.iis.mto.blog.rest.test;

import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

public class FindPostTest extends FunctionalTests {

    private static final String USER_API = "/blog/user";

    @Test
    public void removedUserTriesToFindPost_shouldReturnBadRequest() {
        String removedUserID = "4";
        JSONObject jsonObj = new JSONObject().put("entry", "test entry");
        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj.toString())
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .when()
                .post(USER_API + "/" + removedUserID + "/post");
    }

    @Test
    public void findPostReturnsProperNumberOfLikes() {
        String postId = "1";
        checkLikesCount(postId, 0);

        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .when()
                .post("/blog/user/3/like/" + postId);

        checkLikesCount(postId, 1);
    }

    private void checkLikesCount(String postId, int likesCounter) {
        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .body("likesCount", is(likesCounter))
                .when()
                .get("/blog/post/" + postId);
    }

}
