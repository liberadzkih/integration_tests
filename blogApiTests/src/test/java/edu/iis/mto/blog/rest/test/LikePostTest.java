package edu.iis.mto.blog.rest.test;

import static io.restassured.RestAssured.given;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import io.restassured.http.ContentType;

public class LikePostTest extends FunctionalTests {

    private static final String LIKE_POST = "/blog/user/{userId}/like/{postId}";

    @Test
    public void shouldLikeAndReturnOkStatusWhenConfirmedUser() {
        final String userID = "1";
        final String postID = "2";
        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .when()
                .post(LIKE_POST, userID, postID);
    }

    @Test
    public void shouldNotLikeAndReturnBadRequsestStatusWhenSelfLike() {
        final String userID = "1";
        final String postID = "1";
        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .when()
                .post(LIKE_POST, userID, postID);
    }

    @Test
    public void shouldNotLikeAndReturnBadRequestAccountStatusNotConfirmed() {
        final String userID = "2";
        final String postID = "1";
        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .when()
                .post(LIKE_POST, userID, postID);
    }

    @Test
    public void shouldNotChangeLikeStatusAndReturnOkStatusWhenLikedTwice() {
        final String userID = "1";
        final String postID = "2";
        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .when()
                .post(LIKE_POST, userID, postID);

        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .when()
                .post(LIKE_POST, userID, postID);
    }
}
