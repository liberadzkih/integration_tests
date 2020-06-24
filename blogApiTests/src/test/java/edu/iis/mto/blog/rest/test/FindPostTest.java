package edu.iis.mto.blog.rest.test;

import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItems;

public class FindPostTest extends FunctionalTests {

    private static final String USER_API = "/blog/user/";

    @Test
    public void searchingDeletedUserPostShouldResultedInBadRequestStatusCode() {
        String IDUser = "3";
        given().accept(ContentType.JSON)
               .header("Content-Type", "application/json;charset=UTF-8")
               .expect()
               .log()
               .all()
               .statusCode(HttpStatus.SC_BAD_REQUEST)
               .when()
               .get(USER_API + IDUser + "/post");
    }

    @Test
    public void foundBlogPostShouldReturnNumberOfLikes() {
        String IDUser = "1";
        String IDPost = "1";
        String IDFan = "4";

        given().accept(ContentType.JSON)
               .header("Content-Type", "application/json;charset=UTF-8")
               .expect()
               .log()
               .all()
               .statusCode(HttpStatus.SC_OK)
               .when()
               .post(USER_API + IDFan + "/like/" + IDPost);

        given().accept(ContentType.JSON)
               .header("Content-Type", "application/json;charset=UTF-8")
               .expect()
               .log()
               .all()
               .statusCode(HttpStatus.SC_OK)
               .and()
               .body("likesCount", hasItems(1))
               .when()
               .get(USER_API + IDUser + "/post");
    }
}

