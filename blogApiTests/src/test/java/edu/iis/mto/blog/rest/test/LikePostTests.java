package edu.iis.mto.blog.rest.test;

import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class LikePostTests extends FunctionalTests {

    private static final String LIKE_POST = "/blog/user/{userId}/like/{postId}";

    @Test
    public void returnOKStatusAndLikeWhenUserConfirmed() {
        String userId = "1";
        String postId = "2";
        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .when()
                .post(LIKE_POST, userId, postId);
    }

    @Test
    public void returnForbiddenStatusAndNotLikeWhenUserNotConfirmed() {
        String userId = "2";
        String postId = "1";
        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .when()
                .post(LIKE_POST, userId, postId);
    }

    @Test
    public void returnBadRequestStatusAndNotLikeWhenMyPost() {
        String userId = "1";
        String postId = "1";
        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .when()
                .post(LIKE_POST, userId, postId);
    }

    @Test
    public void returnOKStatusAndNotChangeLikeWhenAlreadyLiked() {
        String userId = "1";
        String postId = "2";
        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .when()
                .post(LIKE_POST, userId, postId);

        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .when()
                .post(LIKE_POST, userId, postId);
    }
}