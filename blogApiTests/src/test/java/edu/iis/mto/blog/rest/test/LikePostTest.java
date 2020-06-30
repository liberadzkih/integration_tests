package edu.iis.mto.blog.rest.test;

import static io.restassured.RestAssured.given;

import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import io.restassured.http.ContentType;

public class LikePostTest extends FunctionalTests {

	private static final String USER_API = "/blog/user";

	@Test
	public void likePostByConfirmedUserReturnsOkStatus() {
		String confirmedId = "1";
		given().accept(ContentType.JSON)
				.header("Content-Type", "application/json;charset=UTF-8")
				.expect()
				.log()
				.all()
				.statusCode(HttpStatus.SC_OK)
				.when()
				.post(USER_API + "/" + confirmedId + "/like/" + "2");
	}

	@Test
	public void likePostByNotConfirmedUserReturnsBadRequest() {
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
				.post(USER_API + "/" + notConfirmedId + "/like/" + "2");
	}

	@Test
	public void likePostByCreatorUserReturnsBadRequest() {
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
				.post(USER_API + "/" + notConfirmedId + "/like/" + "1");
	}

	@Test
	public void likePostTwoTimesReturnsOkStatus() {
		String confirmedId = "1";
		given().accept(ContentType.JSON)
				.header("Content-Type", "application/json;charset=UTF-8")
				.expect()
				.log()
				.all()
				.statusCode(HttpStatus.SC_OK)
				.when()
				.post(USER_API + "/" + confirmedId + "/like/" + "2");

		given().accept(ContentType.JSON)
				.header("Content-Type", "application/json;charset=UTF-8")
				.expect()
				.log()
				.all()
				.statusCode(HttpStatus.SC_OK)
				.when()
				.post(USER_API + "/" + confirmedId + "/like/" + "2");
	}
}
