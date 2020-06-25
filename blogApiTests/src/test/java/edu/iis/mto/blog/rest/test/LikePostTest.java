package edu.iis.mto.blog.rest.test;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.post;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;

public class LikePostTest extends FunctionalTests {

    private static final String USER_API = "/blog/";

    @Test
    public void likePostFromNotConfirmedUserResultInBadRequest() {
        JSONObject jsonObj = new JSONObject().put("entry", "moj post");
        given().accept(ContentType.JSON)
               .header("Content-Type", "application/json;charset=UTF-8")
               .body(jsonObj.toString())
               .expect()
               .log()
               .all()
               .statusCode(400)
               .when()
               .post(USER_API + "user/2/like/1");
    }

    @Test
    public void likePostAgainFromConfirmedUserResultInBadRequest() {
        long confirmedUserId = 1;
        long postId = addPostToBeLiked(confirmedUserId);
        post(USER_API + "user/" + confirmedUserId + "/like/" + postId)
                .then()
                .log()
                .all()
                .statusCode(400)
                .assertThat();
    }

    @Test
    public void likePostTwiceShouldNothingChange() {
        long confirmedUser1Id = 1;
        long confirmedUser2Id = 3;
        long postId = addPostToBeLiked(confirmedUser1Id);
        post(USER_API + "user/" + confirmedUser2Id + "/like/" + postId)
                .then()
                .log()
                .all()
                .statusCode(200)
                .assertThat();
        post(USER_API + "user/" + confirmedUser2Id + "/like/" + postId)
                .then()
                .log()
                .all()
                .statusCode(200)
                .assertThat();
    }


}
