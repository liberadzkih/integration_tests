package edu.iis.mto.blog.rest.test;

import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

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
    public void addUserEmailMustBeUnique() {
        JSONObject jsonObjectNumber1 = new JSONObject()
                .put("email", "noname@domain.com")
                .put("firstName", "John")
                .put("lastName", "Noname");
        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObjectNumber1.toString())
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_CREATED)
                .when()
                .post(USER_API);

        JSONObject jsonObjectNumber2 = new JSONObject()
                .put("email", "noname@domain.com")
                .put("firstName", "Matthew")
                .put("lastName", "Noone");
        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObjectNumber2.toString())
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_CONFLICT)
                .when()
                .post(USER_API);
    }
}
