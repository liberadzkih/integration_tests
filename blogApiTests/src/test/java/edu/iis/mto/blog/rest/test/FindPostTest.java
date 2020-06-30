package edu.iis.mto.blog.rest.test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import io.restassured.http.ContentType;

public class FindPostTest extends FunctionalTests {

	private static final String USER_API = "/blog/user";

	@Test
	public void findPostCreatedByRemovedUserReturnsBadRequest() {
		String removedId = "4";
		JSONObject jsonObj = new JSONObject().put("entry", "test");
		given().accept(ContentType.JSON)
				.header("Content-Type", "application/json;charset=UTF-8")
				.body(jsonObj.toString())
				.expect()
				.log()
				.all()
				.statusCode(HttpStatus.SC_BAD_REQUEST)
				.when()
				.post(USER_API + "/" + removedId + "/post");
	}

	@Test
	public void findPostReturnsProperNumberOfLikes() {
		String postId = "1";
		given().accept(ContentType.JSON)
				.header("Content-Type", "application/json;charset=UTF-8")
				.expect()
				.log()
				.all()
				.statusCode(HttpStatus.SC_OK)
				.body("likesCount", is(0))
				.when()
				.get("/blog/post/" + postId);

		given().accept(ContentType.JSON)
				.header("Content-Type", "application/json;charset=UTF-8")
				.expect()
				.log()
				.all()
				.statusCode(HttpStatus.SC_OK)
				.when()
				.post("/blog/user/3/like/" + postId);

		given().accept(ContentType.JSON)
				.header("Content-Type", "application/json;charset=UTF-8")
				.expect()
				.log()
				.all()
				.statusCode(HttpStatus.SC_OK)
				.body("likesCount", is(1))
				.when()
				.get("/blog/post/" + postId);

	}

}
