package edu.iis.mto.blog.rest.test;

import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class FindUserTest extends FunctionalTests {

    private static final String USER_API = "/blog/user";

    @Test
    public void findUsers_shouldReturnValidNumberOfExistingUsers() {
        int numberOfExistingUsers = 3;
        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .body("size", is(numberOfExistingUsers))
                .when()
                .get(USER_API + "/find?searchString=@");

    }

    @Test
    public void findRemovedUser_shouldReturnEmptySet() {
        String removedUserLastName = "Nowak";
        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .body("size", is(0))
                .when()
                .get(USER_API + "/find?searchString=" + removedUserLastName);
    }

}
