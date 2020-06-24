package edu.iis.mto.blog.rest.test;

import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class LikePostTest extends FunctionalTests {

    private static final String LIKE_POST = "/blog/user/{userId}/like/{postId}";

    @Test
    public void shouldLikeAndReturnOkStatusWhenConfirmedUser() {
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
    public void shouldNotLikeAndReturnForbiddenWhenNotConfirmedUser() {
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
    public void shouldNotLikeAndReturnBadRequestWhenOwnPost() {
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
    public void shouldNotChangeLikeStatusAndReturnOkStatusWhenLikedTwice() {
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
