package edu.iis.mto.blog.rest.test;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

// user id 1L confirmed
// user id 2L not confirmed
// user id 3L confimed
public class LikePostTest extends FunctionalTests {

    private static final String LIKE_POST_API = "/blog/user/{userId}/like/{postId}";
    
    @Test
    public void confirmedUserShouldBeAbleToLikePost() {
        Long postid = createPostReturnId(1L);
        given().accept(ContentType.JSON)
               .header("Content-Type", "application/json;charset=UTF-8")
               .expect()
               .log()
               .all()
               .statusCode(HttpStatus.SC_OK)
               .when()
               .post(LIKE_POST_API, 3L, postid);
    }

    @Test
    public void notConfirmedUserShouldNotBeAbleToLikePost() {
        Long postid = createPostReturnId(1L);
        given().accept(ContentType.JSON)
               .header("Content-Type", "application/json;charset=UTF-8")
               .expect()
               .log()
               .all()
               .statusCode(HttpStatus.SC_BAD_REQUEST)
               .when()
               .post(LIKE_POST_API, 2L, postid);
    }

    @Test
    public void userShouldNotBeAbleToLikeHisOwnPost() {
        Long postid = createPostReturnId(1L);
        given().accept(ContentType.JSON)
               .header("Content-Type", "application/json;charset=UTF-8")
               .expect()
               .log()
               .all()
               .statusCode(HttpStatus.SC_BAD_REQUEST)
               .when()
               .post(LIKE_POST_API, 1L, postid);
    }

    @Test
    public void likingThePostTwiceShouldNotAffectOnStatus() {
        Long postid = createPostReturnId(1L);
        Response response = given().accept(ContentType.JSON)
               .header("Content-Type", "application/json;charset=UTF-8")
               .expect()
               .log()
               .all()
               .statusCode(HttpStatus.SC_OK)
               .when()
               .post(LIKE_POST_API, 3L, postid);
        System.out.println(response);
        response = given().accept(ContentType.JSON)
               .header("Content-Type", "application/json;charset=UTF-8")
               .expect()
               .log()
               .all()
               .statusCode(HttpStatus.SC_OK)
               .when()
               .post(LIKE_POST_API, 3L, postid);
        assertEquals("false", response.getBody().print());
    }

    private Long createPostReturnId(Long userid) {
        JSONObject body = new JSONObject().put("entry", "Irrelevant");
        Response response = given().accept(ContentType.JSON)
                                   .header("Content-Type", "application/json;charset=UTF-8")
                                   .body(body.toString())
                                   .expect()
                                   .log()
                                   .all()
                                   .statusCode(HttpStatus.SC_CREATED)
                                   .when()
                                   .post("/blog/user/{id}/post", userid.toString());
        return response.jsonPath().getLong("id");
    }
}
