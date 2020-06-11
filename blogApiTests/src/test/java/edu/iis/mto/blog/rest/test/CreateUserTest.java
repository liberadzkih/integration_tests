package edu.iis.mto.blog.rest.test;

import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class CreateUserTest extends FunctionalTests {

    private static final String USER_API = "/blog/user";
    private static final String USER_POST = "/blog/user/{ID}/post";

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
    public void shouldGenerate409ResponseWhenDataIntegrituViolationException() {
        JSONObject jsonObj = new JSONObject();
        given().accept(ContentType.JSON)
               .header("Content-Type", "application/json;charset=UTF-8")
               .body(jsonObj.toString())
               .expect()
               .log()
               .all()
               .statusCode(HttpStatus.SC_CONFLICT)
               .when()
               .post(USER_API);
    }

    @Test
    public void shouldGenerateConflictWhenUserEmailAlreadyInUse() {
        JSONObject jsonObj = new JSONObject().put("email", "tracy1@domain.com");
        given().accept(ContentType.JSON)
               .header("Content-Type", "application/json;charset=UTF-8")
               .body(jsonObj.toString())
               .expect()
               .log()
               .all()
               .statusCode(HttpStatus.SC_CONFLICT)
               .when()
               .post(USER_API);
    }

    @Test
    public void postShouldNotBeAddedWhenNotConfirmedUser() {
        final String ID = "1";
        JSONObject jsonObj = new JSONObject().put("entry", "ENTRY_POST");
        given().accept(ContentType.JSON)
               .header("Content-Type", "application/json;charset=UTF-8")
               .body(jsonObj.toString())
               .expect()
               .log()
               .all()
               .statusCode(HttpStatus.SC_FORBIDDEN)
               .when()
               .post(USER_POST, ID);
    }

    @Test
    public void postShouldBeAddedWhenConfirmedUser() {
        final String ID = "2";
        JSONObject jsonObj = new JSONObject().put("entry", "ENTRY_POST");
        given().accept(ContentType.JSON)
               .header("Content-Type", "application/json;charset=UTF-8")
               .body(jsonObj.toString())
               .expect()
               .log()
               .all()
               .statusCode(HttpStatus.SC_CREATED)
               .when()
               .post(USER_POST, ID);
    }
}
