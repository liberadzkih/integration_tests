package edu.iis.mto.blog.rest.test;

import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class LikePostTest extends FunctionalTests {
    private static final String USER_API = "/blog/user/{userId}/like/{postID}";

    @Test
    public void confirmedUserCanLikePostShouldReturnOkStatus() {
        String userId = "1";
        String postId = "2";
        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .when()
                .post(USER_API, userId, postId);
    }

    @Test
    public void newUserCannotLikePostShouldReturnBadRequestStatus() {
        String userId = "2";
        String postId = "1";
        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .when()
                .post(USER_API, userId, postId);
    }

    @Test
    public void confirmedUserCannotLikeOwnPostShouldReturnBadRequestStatus() {
        String userId = "1";
        String postId = "1";
        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .when()
                .post(USER_API, userId, postId);
    }

    @Test
    public void confirmedUserCanLikeOnePostTwiceShouldReturnOkStatus() {
        String userId = "1";
        String postId = "2";
        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .when()
                .post(USER_API, userId, postId);

        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .when()
                .post(USER_API, userId, postId);
    }
}


