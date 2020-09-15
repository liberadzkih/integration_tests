package edu.iis.mto.blog.rest.test;

import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class SearchForUserTest extends FunctionalTests {

    private static final String USER_FIND_API = "/blog/user/find";

    @Test
    public void shouldReturnUsersWithoutRemovedUser() {
        given().accept(ContentType.JSON)
               .header("Content-Type", "application/json;charset=UTF-8")
               .param("searchString", "@")
               .expect()
               .log()
               .all()
               .statusCode(HttpStatus.SC_OK)
               .and()
               .body("size", is(4))
               .when()
               .get(USER_FIND_API);
    }

    @Test
    public void removedUserShouldBeNotVisible() {
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
               .get(USER_FIND_API);
    }
}
