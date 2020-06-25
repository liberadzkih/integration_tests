package edu.iis.mto.blog.rest.test;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.post;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class FindPost extends FunctionalTests {


    private static final String USER_API = "/blog/";

    @Test
    public void  postsOfRemovedUserCannotBeFound() {
        long removedUserId = 3;
        String result = get(USER_API + "user/" + removedUserId + "/post").getBody().asString();
        assertThat(result,is("[]"));
    }

    @Test
    public void getPostShouldReturnPostWithCorrectNumberOfLikes() {
        long confirmedUser1Id = 1;
        long confirmedUser2Id = 3;
        long postId = addPostToBeLiked(confirmedUser1Id);
        post(USER_API + "user/" + confirmedUser2Id + "/like/" + postId)
                .then()
                .log()
                .all()
                .statusCode(200)
                .assertThat();

        String json = get(USER_API + "post/" + postId).getBody().asString();
        JSONObject result = new JSONObject(json);
        int count = (int) result.get("likesCount");
        assertThat(count, is(1));
    }

}
