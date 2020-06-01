package edu.iis.mto.blog.rest.test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

import groovy.transform.ToString;
import io.restassured.path.json.JsonPath;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import io.restassured.http.ContentType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.annotation.Target;
import java.util.List;
import java.util.stream.Collectors;

public class CreateUserTest extends FunctionalTests {

    private static final String USER_API = "/blog/user";

    @Test
    public void createUserWithProperDataReturnsCreatedStatus() {
        JSONObject jsonObj = new JSONObject().put("email", "tracy1@domain.com");
        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj.toString())
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_CREATED)
                .when()
                .post(USER_API);
    }

    @Test
    public void shouldNotAllowUserCreationWithOccupiedEmail() {
        JSONObject jsonObject = new JSONObject()
                .put("email", "215920@edu.p.lodz.pl")
                .put("firstName", "Dawid")
                .put("lastName", "Witaszek");

        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObject.toString())
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_CONFLICT)
                .when()
                .post(USER_API);
    }

    @Test
    public void shouldAllowCreatingPostByConfirmedUser() {
        JSONObject jsonObject = new JSONObject()
                .put("entry", "DEADBEEF");

        given()
                .accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObject.toString())
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_CREATED)
                .when()
                .post(USER_API + "/1/post");
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 3})
    public void shouldNotAllwoCreatingPostByUnconfirmedUser(int userId) {
        JSONObject jsonObject = new JSONObject()
                .put("entry", "DEADBEEF");

        given()
                .accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObject.toString())
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .when()
                .post(USER_API + "/" + userId + "/post");
    }

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
    public void shouldNotFindRemovedUser() {
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

    @Test
    public void canSearchForUsers(){
        JSONObject jsonObject = new JSONObject();
        String userEmail = "215920@edu.p.lodz.pl";
        given()
                .accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObject.toString())
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .when()
                .get(USER_API + "/find?searchString="+ userEmail)
                .then()
                .assertThat()
                .body("size()",is(1));
    }

    @Test
    public void shouldOmitRemovedUsers(){
        JSONObject jsonObject = new JSONObject();
        String userEmail = "@edu.p.lodz.pl";
        given()
                .accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObject.toString())
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .when()
                .get(USER_API + "/find?searchString="+ userEmail)
                .then()
                .assertThat()
                .body("size()",is(4));
    }
}
