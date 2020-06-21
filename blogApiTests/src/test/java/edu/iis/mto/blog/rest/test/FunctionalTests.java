package edu.iis.mto.blog.rest.test;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;

import io.restassured.RestAssured;

import static io.restassured.RestAssured.given;

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


}
