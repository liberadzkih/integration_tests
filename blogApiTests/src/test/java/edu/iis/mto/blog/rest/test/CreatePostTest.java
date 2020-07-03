package edu.iis.mto.blog.rest.test;

import static io.restassured.RestAssured.given;

import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import io.restassured.http.ContentType;

public class CreatePostTest extends FunctionalTests {

	private static final String USER_API = "/blog/user";

	@Test
	public void createPostByConfirmedUserReturnsCreatedStatus() {
		String confirmedId = "1";
		JSONObject jsonObj = new JSONObject().put("entry", "test");
		given().accept(ContentType.JSON)
				.header("Content-Type", "application/json;charset=UTF-8")
				.body(jsonObj.toString())
				.expect()
				.log()
				.all()
				.statusCode(HttpStatus.SC_CREATED)
				.when()
				.post(USER_API + "/" + confirmedId + "/post");
	}

	@Test
	public void createPostByNotConfirmedUserReturnsBadRequest() {
		String notConfirmedId = "2";
		JSONObject jsonObj = new JSONObject().put("entry", "test");
		given().accept(ContentType.JSON)
				.header("Content-Type", "application/json;charset=UTF-8")
				.body(jsonObj.toString())
				.expect()
				.log()
				.all()
				.statusCode(HttpStatus.SC_BAD_REQUEST)
				.when()
				.post(USER_API + "/" + notConfirmedId + "/post");
	}
}
