package edu.iis.mto.blog.rest.test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import io.restassured.http.ContentType;

public class FindUserTest extends FunctionalTests {

	private static final String USER_API = "/blog/user";

	@Test
	public void findUserWithRemovedUserIdReturnsNoRecords() {
		String removedUserName = "Jacek";
		given().accept(ContentType.JSON)
				.header("Content-Type", "application/json;charset=UTF-8")
				.expect()
				.log()
				.all()
				.statusCode(HttpStatus.SC_OK)
				.body("size", is(0))
				.when()
				.get(USER_API + "/find?searchString=" + removedUserName);
	}

	@Test
	public void findUserReturnsProperNumberOfUser() {
		given().accept(ContentType.JSON)
				.header("Content-Type", "application/json;charset=UTF-8")
				.expect()
				.log()
				.all()
				.statusCode(HttpStatus.SC_OK)
				.body("size", is(3))
				.when()
				.get(USER_API + "/find?searchString=@");

	}

}
