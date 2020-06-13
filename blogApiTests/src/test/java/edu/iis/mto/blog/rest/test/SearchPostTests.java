package edu.iis.mto.blog.rest.test;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class SearchPostTests extends FunctionalTests {
    private static final Long CONFIRMED_USER_ID = 1L;
    private static final Long SECOND_CONFIRMED_USER_ID = 3L;
    private static final Long DELETED_USER_ID = 4L;
    private static final String USER_API = "/blog/user/{USER_ID}/post";
    private static final String POST_TITLE = "likeCountTestTitle";

    @Test
    public void gettingUserPostsForConfirmedUserShouldBeSuccessfull() {
        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .when()
                .get(getUserApiForUserId(CONFIRMED_USER_ID));
    }

    @Test
    public void gettingUserPostsForDeletedUserShouldNotBeSuccessfull() {
        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .when()
                .get(getUserApiForUserId(DELETED_USER_ID));
    }

    @Test
    public void gettingUserPostsShouldReturnCorrectNumberOfLikes() {
        Long postId = createPostForUserId(CONFIRMED_USER_ID);
        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .when()
                .get(getUserApiForUserId(CONFIRMED_USER_ID))
                .then()
        .assertThat()
        .body("find {it.entry == '" + POST_TITLE + "' }.likesCount", is(0));

        likePostForUserIdAndPostId(SECOND_CONFIRMED_USER_ID, postId);
        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .when()
                .get(getUserApiForUserId(CONFIRMED_USER_ID))
                .then()
                .assertThat()
                .body("find {it.entry == '" + POST_TITLE + "' }.likesCount", is(1));

        likePostForUserIdAndPostId(SECOND_CONFIRMED_USER_ID, postId);
        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .when()
                .get(getUserApiForUserId(CONFIRMED_USER_ID))
                .then()
                .assertThat()
                .body("find {it.entry == '" + POST_TITLE + "' }.likesCount", is(1));
    }

    private String getUserApiForUserId(Long userId) {
        return USER_API.replace("{USER_ID}", userId.toString());
    }

    private Long createPostForUserId(Long confirmedUserId) {
        JSONObject body = new JSONObject().put("entry", POST_TITLE);
        Response response = given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .body(body.toString())
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_CREATED)
                .when()
                .post("/blog/user/{USER_ID}/post".replace("{USER_ID}", confirmedUserId.toString()));
        return response.jsonPath().getLong("id");
    }

    private void likePostForUserIdAndPostId(Long userId, Long postId) {
        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .when()
                .post("/blog/user/{USER_ID}/like/{POST_ID}".replace("{USER_ID}", userId.toString()).replace("{POST_ID}", postId.toString()));
    }
}
