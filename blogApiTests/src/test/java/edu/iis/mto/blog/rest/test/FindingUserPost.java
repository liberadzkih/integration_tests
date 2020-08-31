package edu.iis.mto.blog.rest.test;

import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItems;

public class FindingUserPost extends FunctionalTests {

    private static final String USER_API = "/blog/user/{id}/";

    @Test void tryingToFindARemovedUsersPostShouldResultInBadRequest() {
        given().accept(ContentType.JSON)
               .header("Content-Type", "application/json;charset=UTF-8")
               .expect()
               .log()
               .all()
               .statusCode(HttpStatus.SC_BAD_REQUEST)
               .when()
               .get(USER_API, 3);
    }

    @Test void findingCorrectPostShouldReturnItsLikesCount() {
        given().accept(ContentType.JSON)
               .header("Content-Type", "application/json;charset=UTF-8")
               .expect()
               .log()
               .all()
               .statusCode(HttpStatus.SC_OK)
               .when()
               .post(USER_API + "like/{postid}", 1, 2);

        given().accept(ContentType.JSON)
               .header("Content-Type", "application/json;charset=UTF-8")
               .expect()
               .log()
               .all()
               .statusCode(HttpStatus.SC_OK)
               .body("likesCount", hasItems(1))
               .when()
               .get(USER_API + "post", 2);
    }
}
