package edu.iis.mto.blog.rest.test;

import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

public class SearchUserTest extends FunctionalTests {
    @Test
    public void searchingUsersShouldFindOnlyNotRemovedUsers() {
        given().accept(ContentType.JSON)
               .header("Content-Type", "application/json;charset=UTF-8")
               .expect()
               .log()
               .all()
               .statusCode(HttpStatus.SC_OK)
               .and()
               .body("size",is(2))
               .when()
               .get("/blog/user/find?searchString=Laura");
    }
}
