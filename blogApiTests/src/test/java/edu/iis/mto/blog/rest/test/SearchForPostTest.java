package edu.iis.mto.blog.rest.test;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.hamcrest.Matchers;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;

//user id 4L status is removed
public class SearchForPostTest extends FunctionalTests {

    private static final String USER_POST_API = "/blog/user/{userId}/post";

    @Test
    public void searchingForUserWithStatusRemovedPostsShouldReturn400BadRequest() {
        given().accept(ContentType.JSON)
               .header("Content-Type", "application/json;charset=UTF-8")
               .expect()
               .log()
               .all()
               .statusCode(HttpStatus.SC_BAD_REQUEST)
               .when()
               .get(USER_POST_API, 4L);
    }

    @Test
    public void checkIfLikeCounterIsWorkingProperly() {
        //first like
        given().accept(ContentType.JSON)
               .header("Content-Type", "application/json;charset=UTF-8")
               .expect()
               .log()
               .all()
               .statusCode(HttpStatus.SC_OK)
               .when()
               .post("/blog/user/{userId}/like/{postId}", 1L, 2);
        //second like
        given().accept(ContentType.JSON)
               .header("Content-Type", "application/json;charset=UTF-8")
               .expect()
               .log()
               .all()
               .statusCode(HttpStatus.SC_OK)
               .when()
               .post("/blog/user/{userId}/like/{postId}", 3L, 2);
        given().accept(ContentType.JSON)
               .header("Content-Type", "application/json;charset=UTF-8")
               .expect()
               .log()
               .all()
               .statusCode(HttpStatus.SC_OK)
               .when()
               .get(USER_POST_API, "5")
               .then()
               .assertThat()
               .body("find {it.entry == 'entry' }.likesCount", is(2));

    }
}
