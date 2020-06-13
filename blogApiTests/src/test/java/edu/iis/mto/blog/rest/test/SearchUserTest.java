package edu.iis.mto.blog.rest.test;

import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class SearchUserTest extends FunctionalTests {
    private static final String USER_API = "/blog/user/find";

    @Test
    public void shouldFindOnlyNonRemovedUsers() {
        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .formParam("searchString", "p.lodz.pl")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .when()
                .get(USER_API)
                .then()
                .assertThat()
                .body("size()", is(1));
    }

    @Test
    public void shouldFindNoUsers() {
        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .formParam("searchString", "anythingElseOtherThanWhatIsInTheDatabase")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .when()
                .get(USER_API)
                .then()
                .assertThat()
                .body("size()", is(0));
    }
}
