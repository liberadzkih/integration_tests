package edu.iis.mto.blog.rest.test;

import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItems;

public class FindPostTest extends FunctionalTests {

    private static final String USER_API = "/blog/user/";

    @Test
    public void findRemovedUserPostReturnsBadRequestStatus() {
        String removedUserId = "3";
        given().accept(ContentType.JSON)
               .header("Content-Type", "application/json;charset=UTF-8")
               .expect()
               .log()
               .all()
               .statusCode(HttpStatus.SC_BAD_REQUEST)
               .when()
               .get(USER_API + removedUserId + "/post");
    }

    @Test
    public void findUserPostReturnsProperNumberOfLikes() {
        String otherUserId = "1";
        String postId = "1";
        String authorId = "2";

        given().accept(ContentType.JSON)
               .header("Content-Type", "application/json;charset=UTF-8")
               .expect()
               .log()
               .all()
               .statusCode(HttpStatus.SC_OK)
               .when()
               .post(USER_API + otherUserId + "/like/" + postId);

        given().accept(ContentType.JSON)
               .header("Content-Type", "application/json;charset=UTF-8")
               .expect()
               .log()
               .all()
               .statusCode(HttpStatus.SC_OK)
               .and()
               .body("likesCount", hasItems(1))
               .when()
               .get(USER_API + authorId + "/post");
    }
}
