package edu.iis.mto.blog.rest.test;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.get;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class FindUserTest extends FunctionalTests {

    private static final String USER_API = "/blog/";

    @Test
    public void  findUserByStringShoudNotReturnRemovedUsers() {
        String json = get(USER_API + "user/find?searchString=testUser" ).getBody().asString();
        assertFalse(json.contains("id:4"));
    }


}
