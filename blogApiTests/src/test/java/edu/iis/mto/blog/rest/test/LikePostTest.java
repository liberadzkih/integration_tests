package edu.iis.mto.blog.rest.test;

import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class LikePostTest extends FunctionalTests {

    private static final String USER_API = "/blog/user/";

    @Test
    public void likeFromConfirmedUserShouldReturnOkStatus() {
        String IDUser = "1";
        String IDPost = "2";
        given().accept(ContentType.JSON)
               .header("Content-Type", "application/json;charset=UTF-8")
               .expect()
               .log()
               .all()
               .statusCode(HttpStatus.SC_OK)
               .when()
               .post(USER_API + IDUser + "/like/" + IDPost);
    }

    @Test
    public void selfLikeShouldReturnBadRequestStatus() {
        String IDUser = "1";
        String IDPost = "1";
        given().accept(ContentType.JSON)
               .header("Content-Type", "application/json;charset=UTF-8")
               .expect()
               .log()
               .all()
               .statusCode(HttpStatus.SC_BAD_REQUEST)
               .when()
               .post(USER_API + IDUser + "/like/" + IDPost);
    }

    @Test
    public void likeFromNotConfirmedUserShouldReturnBadRequestStatus() {
        String IDUser = "2";
        String IDPost = "1";
        given().accept(ContentType.JSON)
               .header("Content-Type", "application/json;charset=UTF-8")
               .expect()
               .log()
               .all()
               .statusCode(HttpStatus.SC_BAD_REQUEST)
               .when()
               .post(USER_API + IDUser + "/like/" + IDPost);
    }

    @Test
    public void twoLikesForTheSamePostShouldReturnOKStatus() {
        String IDUser = "1";
        String IDPost = "2";
        given().accept(ContentType.JSON)
               .header("Content-Type", "application/json;charset=UTF-8")
               .expect()
               .log()
               .all()
               .statusCode(HttpStatus.SC_OK)
               .when()
               .post(USER_API + IDUser + "/like/" + IDPost);

        given().accept(ContentType.JSON)
               .header("Content-Type", "application/json;charset=UTF-8")
               .expect()
               .log()
               .all()
               .statusCode(HttpStatus.SC_OK)
               .when()
               .post(USER_API + IDUser + "/like/" + IDPost);
    }
}
