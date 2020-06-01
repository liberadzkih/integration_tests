package edu.iis.mto.blog.rest.test;

import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static edu.iis.mto.blog.rest.test.CreateUserTest.USER_API;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AddLikeTest {
    @Test
    public void userLikePost() {
        JSONObject jsonObject = new JSONObject();

        given()
                .accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObject.toString())
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .when()
                .post(USER_API + "/1/like/1");
    }

    @Test
    public void userCannotAddLikeToHisOwnPost() {
        JSONObject jsonObject = new JSONObject();

        given()
                .accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObject.toString())
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .when()
                .post(USER_API + "/2/like/1");
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 3})
    public void unconfirmedUserCannotAddLike(int userId) {
        JSONObject jsonObject = new JSONObject();

        given()
                .accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObject.toString())
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .when()
                .post(USER_API + "/" + userId + "/like/1");
    }

    @Test
    public void cannotLikeTwoTimes() {
        JSONObject jsonObject = new JSONObject();

        given()
                .accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObject.toString())
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .when()
                .post(USER_API + "/1/like/1");

        String string = given()
                .accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObject.toString())
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .when()
                .post(USER_API + "/1/like/1")
                .then()
                .assertThat()
                .extract()
                .asString();

        assertEquals("false", string);
    }
}
