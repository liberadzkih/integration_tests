package edu.iis.mto.blog.rest.test;

import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class LikePostTest extends FunctionalTests {

    private static final String USER_API = "/blog/user/{id}/like/{postid}";

    @Test void confirmedUserShouldBeAbleToLikeOtherUserPosts() {
        int confirmedUserId = 1;
        int unconfirmedUserPostId = 2;
        given().accept(ContentType.JSON)
               .header("Content-Type", "application/json;charset=UTF-8")
               .expect()
               .log()
               .all()
               .statusCode(HttpStatus.SC_OK)
               .when()
               .post(USER_API, confirmedUserId, unconfirmedUserPostId);
    }

    @Test void unconfirmedUserShouldNotBeAbleToLikePosts() {
        int unconfirmedUserId = 2;
        int confirmedUserPostId = 1;
        given().accept(ContentType.JSON)
               .header("Content-Type", "application/json;charset=UTF-8")
               .expect()
               .log()
               .all()
               .statusCode(HttpStatus.SC_BAD_REQUEST)
               .when()
               .post(USER_API, unconfirmedUserId, confirmedUserPostId);
    }

    @Test void confirmedUserShouldNotBeAbleToLikeHisOwnPosts() {
        int confirmedUserId = 1;
        int confirmedUserPostId = 1;
        given().accept(ContentType.JSON)
               .header("Content-Type", "application/json;charset=UTF-8")
               .expect()
               .log()
               .all()
               .statusCode(HttpStatus.SC_BAD_REQUEST)
               .when()
               .post(USER_API, confirmedUserId, confirmedUserPostId);
    }

    @Test void likingTheSamePostMultipleTimesShouldNotChangeItsStatus() {
        int confirmedUserId = 1;
        int unConfirmedUserPostId = 2;
        given().accept(ContentType.JSON)
               .header("Content-Type", "application/json;charset=UTF-8")
               .expect()
               .log()
               .all()
               .statusCode(HttpStatus.SC_OK)
               .when()
               .post(USER_API, confirmedUserId, unConfirmedUserPostId);
        given().accept(ContentType.JSON)
               .header("Content-Type", "application/json;charset=UTF-8")
               .expect()
               .log()
               .all()
               .statusCode(HttpStatus.SC_OK)
               .when()
               .post(USER_API, confirmedUserId, unConfirmedUserPostId);
    }
}
