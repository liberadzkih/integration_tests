package edu.iis.mto.blog.rest.test;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PostLikeTest extends FunctionalTests {
    private static final Long CONFIRMED_USER_ID = 1L;
    private static final Long NOT_CONFIRMED_USER_ID = 2L;
    private static final Long SECOND_CONFIRMED_USER_ID = 3L;
    private static final String USER_API = "/blog/user/{USER_ID}/like/{POST_ID}";

    @Test
    public void confirmedUserShouldBeAbleToLikeOtherPersonPost() {
        Long postId = createPostForUserId(CONFIRMED_USER_ID);
        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .when()
                .post(getUserApiForUserIdAndPostId(SECOND_CONFIRMED_USER_ID, postId));
    }

    @Test
    public void notConfirmedUserShouldNotBeAbleToLikeOtherPersonPost() {
        Long postId = createPostForUserId(CONFIRMED_USER_ID);
        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .when()
                .post(getUserApiForUserIdAndPostId(NOT_CONFIRMED_USER_ID, postId));
    }

    @Test
    public void repeatedLikesFromSameUserShouldNotAdded() {
        Long postId = createPostForUserId(CONFIRMED_USER_ID);
        Response response = given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .when()
                .post(getUserApiForUserIdAndPostId(SECOND_CONFIRMED_USER_ID, postId));
        assertEquals("true", response.body().asString());
        response = given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .when()
                .post(getUserApiForUserIdAndPostId(SECOND_CONFIRMED_USER_ID, postId));
        assertEquals("false", response.body().asString());
    }

    private String getUserApiForUserIdAndPostId(Long userId, Long postId) {
        return USER_API.replace("{USER_ID}", userId.toString()).replace("{POST_ID}", postId.toString());
    }

    private Long createPostForUserId(Long confirmedUserId) {
        JSONObject body = new JSONObject().put("entry", "Irrelevant");
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
}
