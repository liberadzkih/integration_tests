package edu.iis.mto.blog.rest.test;

import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

public class SearchBlogPostTest extends FunctionalTests {
    @Test
    public void searchingPostsOfRemovedUserShouldResultWithFailure() {
        given().accept(ContentType.JSON)
               .header("Content-Type", "application/json;charset=UTF-8")
               .expect()
               .log()
               .all()
               .statusCode(HttpStatus.SC_BAD_REQUEST)
               .when()
               .post("/blog/user/4/post");
    }
    @Test
    public void searchingPostsOfNotRemovedUserShouldResultWithSuccessAndProperCountOfLikes() {
        given().accept(ContentType.JSON)
               .header("Content-Type", "application/json;charset=UTF-8")
               .expect()
               .log()
               .all()
               .statusCode(HttpStatus.SC_OK)
               .body("likesCount",is(0))
               .when()
               .get("/blog/post/1");

    }

    @Test
    public void searchingPostsOfNotRemovedUserShouldResultWithSuccessAndReturnProperCountOfLikes() {

        given().accept(ContentType.JSON)
               .header("Content-Type", "application/json;charset=UTF-8")
               .expect()
               .log()
               .all()
               .statusCode(HttpStatus.SC_OK)
               .when()
               .post("/blog/user/1/like/2");

        given().accept(ContentType.JSON)
               .header("Content-Type", "application/json;charset=UTF-8")
               .expect()
               .log()
               .all()
               .statusCode(HttpStatus.SC_OK)
               .when()
               .post("/blog/user/3/like/2");

        given().accept(ContentType.JSON)
               .header("Content-Type", "application/json;charset=UTF-8")
               .expect()
               .log()
               .all()
               .statusCode(HttpStatus.SC_OK)
               .body("likesCount",is(2))
               .when()
               .get("/blog/post/2");

    }

}
