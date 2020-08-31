package edu.iis.mto.blog.rest.test;

import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
public class FindingUsers extends FunctionalTests {

    private static final String USER_API = "/blog/user/find";

    @Test void findingAllUsersShouldNotReturnRemovedOnes() {
        given().accept(ContentType.JSON)
               .header("Content-Type", "application/json;charset=UTF-8")
               .param("searchString", "")
               .expect()
               .log()
               .all()
               .statusCode(HttpStatus.SC_OK)
               .body("size()", equalTo(2))
               .when()
               .get(USER_API);
    }
}
