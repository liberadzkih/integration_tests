package edu.iis.mto.blog.rest.test;

import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItems;

public class FindPostTest extends FunctionalTests {

    private static final String USER_API = "/blog/user/{userId}/post";
    private static final String USER_API_POST = "/blog/user/{fanId}/like/{postId}";

    @Test
    public void searchBlogShouldReturnCorrectNumberOfLikes() {
        String userId = "2";
        String postId = "2";
        String whoLikePostId = "1";

        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .when()
                .post(USER_API_POST, whoLikePostId, postId);

        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("likesCount", hasItems(1))
                .when()
                .get(USER_API, userId);
    }

    @Test
    public void searchPostUserNotExistShouldReturnBadRequestStatus() {
        String userId = "4";
        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .when()
                .get(USER_API, userId);
    }
}
