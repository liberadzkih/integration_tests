package edu.iis.mto.blog.rest.test;

import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class FindPostTest extends FunctionalTests {

    private static final String USER_API_FIND = "/blog/user/{userId}/post";
    private static final String USER_API_LIKE = "/blog/user/{userId}/like/{postId}";

    @Test
    public void shouldReturnBadRequestWhenSearchedForRemovedUserPost() {
        final int user_id = 4;
        given().accept(ContentType.JSON)
               .header("Content-Type", "application/json;charset=UTF-8")
               .expect()
               .log()
               .all()
               .statusCode(HttpStatus.SC_BAD_REQUEST)
               .when()
               .get(USER_API_FIND, user_id);
    }

    @Test
    public void shouldReturnOkWithProperLikeCounter() {
        final int user_id = 1;
        final int post_id = 1;
        final int user_like_id = 3;

        given().accept(ContentType.JSON)
               .header("Content-Type", "application/json;charset=UTF-8")
               .expect()
               .log()
               .all()
               .statusCode(HttpStatus.SC_OK)
               .when()
               .post(USER_API_LIKE, user_like_id, postId);

        given().accept(ContentType.JSON)
               .header("Content-Type", "application/json;charset=UTF-8")
               .expect()
               .log()
               .all()
               .body("likesCount", hasItem(1))
               .statusCode(HttpStatus.SC_OK)
               .when()
               .get(USER_API_FIND, user_id);
    }
}
