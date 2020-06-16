package edu.iis.mto.blog.rest.test;

import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

public class FindUserTest extends FunctionalTests {
    private static final String USER_API = "/blog/user/find";

    @Test
    public void findUsersByFirstNameShouldReturnOneUser() {
        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .param("searchString", "Brian")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("size", is(1))
                .when()
                .get(USER_API);
    }

    @Test
    public void findUsersFirstNameShouldReturnZeroUsers() {
        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .param("searchString", "Jan")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("size", is(0))
                .when()
                .get(USER_API);
    }

    @Test
    public void findUsersLastNameShouldReturnOneUsers() {
        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .param("searchString", "Ste")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("size", is(1))
                .when()
                .get(USER_API);
    }

    @Test
    public void findUsersEmailShouldReturnThreeUsers() {
        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .param("searchString", "@")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("size", is(3))
                .when()
                .get(USER_API);
    }
}
