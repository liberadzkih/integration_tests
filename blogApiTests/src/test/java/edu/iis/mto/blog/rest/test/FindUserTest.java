package edu.iis.mto.blog.rest.test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import io.restassured.http.ContentType;

public class FindUserTest extends FunctionalTests {

    private static final String FIND_API = "/blog/user/find";

    @Test
    public void shouldReturnOnlyNewAndConfirmedUsersWhenAdded() {
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
                .get(FIND_API);
    }

    @Test
    public void shouldUserNotBeVisibleInResultsWhenRemovd() {
        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .param("searchString", "REMOVED")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("size", is(0))
                .when()
                .get(FIND_API);
    }
}
