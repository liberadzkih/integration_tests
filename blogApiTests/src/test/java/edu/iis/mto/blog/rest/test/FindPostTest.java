package edu.iis.mto.blog.rest.test;

import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static edu.iis.mto.blog.rest.test.CreateUserTest.USER_API;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FindPostTest {
    @Test
    public void canFindUsersPosts() {
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
                .get(USER_API + "/2/post")
                .then()
                .assertThat()
                .body("size()", is(1));
    }

    @Test
    public void shouldNotFindRemovedUsersPosts() {
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
                .get(USER_API + "/3/post");
    }

    @Test
    public void shouldReturnCorrectAmountOfLikes(){
        JSONObject jsonObject = new JSONObject();
        PersonData[] personData = given()
                .accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObject.toString())
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .when()
                .get(USER_API + "/2/post")
                .then()
                .extract()
                .as(PersonData[].class);
        int likesCount = personData[0].getLikesCount();
        assertEquals(1, likesCount);
    }
}
