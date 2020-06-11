package edu.iis.mto.blog.rest.test;

import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.Test;

import static org.hamcrest.core.Is.is;

public class FindUsersTest extends FunctionalTests {

    private static final String USER_API_FIND = "/blog/user/find";

    @Test
    public shouldReturnOnlyNewAndConfirmedUsersWhenAdded() {
              .given().accept(ContentType.JSON)
                      .header("Content-Type", "application/json;charset=UTF-8")
                      .param("searchString", "@")
                      .expect()
                      .log()
                      .all()
                      .statusCode(HttpStatus.SC_OK)
                      .and()
                      .body("size", is(3))
                      .when()
                      .get(USER_API_FIND);
    }

    @Test
    public void shouldUserNotBeVisibleInResultsWhenRemovd() {
       .given().accept(ContentType.JSON)
               .header("Content-Type", "application/json;charset=UTF-8")
               .param("searchString", "removed")
               .expect()
               .log()
               .all()
               .statusCode(HttpStatus.SC_OK)
               .and()
               .body("size", is(0))
               .when()
               .get(USER_API_FIND);
    }
}
