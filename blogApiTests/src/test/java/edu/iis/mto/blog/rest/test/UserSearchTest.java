package edu.iis.mto.blog.rest.test;

import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static edu.iis.mto.blog.rest.test.CreateUserTest.USER_API;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class UserSearchTest {
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
