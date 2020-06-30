package edu.iis.mto.blog.rest.test;

import static io.restassured.RestAssured.given;

import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import io.restassured.http.ContentType;

public class FindPostTest extends FunctionalTests {

    private static final String POST_API = "/blog/user/{userId}/post";
    private static final String LIKE_API = "/blog/user/{userId}/like/{postId}";

    @Test
    public void shouldReturnBadRequestWhenSearchingForPostFromUserWithAccountStatusRemoved() {
        final int user_id = 3;
        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .when()
                .get(POST_API, user_id);
    }
    @Test
    public void shouldReturnOkWithProperLikeCounter() {
        final int post_id = 1;
        final int user_like_id = 4;

        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .when()
                .post(LIKE_API, user_like_id, post_id);

        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .body("likesCount", Matchers.hasItem(1))
                .statusCode(HttpStatus.SC_OK)
                .when()
                .get(POST_API, post_id);
    }

}
