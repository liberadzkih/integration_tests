package edu.iis.mto.blog.rest.test;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;

import io.restassured.RestAssured;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class FunctionalTests {

    @BeforeAll
    public static void setup() {
        String port = System.getProperty("server.port");
        if (port == null) {
            RestAssured.port = Integer.valueOf(8080);
        } else {
            RestAssured.port = Integer.valueOf(port);
        }

        String basePath = System.getProperty("server.base");
        if (basePath == null) {
            basePath = "";
        }
        RestAssured.basePath = basePath;

        String baseHost = System.getProperty("server.host");
        if (baseHost == null) {
            baseHost = "http://localhost";
        }
        RestAssured.baseURI = baseHost;
    }

    public Response addUser(String email){
        JSONObject user = new JSONObject().put("email",email);
        return given().accept(ContentType.JSON)
                      .header("Content-Type", "application/json;charset=UTF-8")
                      .body(user.toString())
                      .expect()
                      .log()
                      .all()
                      .when()
                      .post("/blog/user");
    }

    public long getIdFromResponse(Response response) {
        JSONObject result = new JSONObject(response.asString());
        return result.getLong("id");
    }

    public long addPostToBeLiked(long userId) {
        JSONObject post = new JSONObject().put("entry", "mój post");
        Response postResp = given().accept(ContentType.JSON)
                                   .header("Content-Type", "application/json;charset=UTF-8")
                                   .body(post.toString())
                                   .expect()
                                   .log()
                                   .all()
                                   .statusCode(HttpStatus.SC_CREATED)
                                   .when()
                                   .post("blog/user/" + userId + "/post");

        long postId = getIdFromResponse(postResp);

        get( "blog/post/" + postId)
                .then()
                .log()
                .all()
                .statusCode(200)
                .assertThat()
                .body("entry", equalTo("mój post"));
        return postId;
    }

}
