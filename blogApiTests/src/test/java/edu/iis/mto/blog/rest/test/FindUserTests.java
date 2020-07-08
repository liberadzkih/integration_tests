package edu.iis.mto.blog.rest.test;

import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static org.hamcrest.core.Is.is;
import static io.restassured.RestAssured.given;

public class FindUserTests extends FunctionalTests {

    private static final String USER_API_FIND = "/blog/user/find";

    @Test
    public void returnNewAndConfirmedUsers() {
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
                .get(USER_API_FIND);
    }

    @Test
    public void userNotShownInResultsWhenRemoved() {
       given().accept(ContentType.JSON)
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