package lib;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.http.Headers;
import org.junit.jupiter.api.BeforeAll;

import java.util.Map;

import static org.hamcrest.Matchers.hasKey;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BaseTestCase {
    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "https://playground.learnqa.ru/api_dev/";
    }
    protected String getHeader(Response Response, String name) {
        Headers headers = Response.getHeaders();

        assertTrue(headers.hasHeaderWithName(name), "Response doesn't have header with name" + name);
        return headers.getValue(name);
    }
    protected String getCookie(Response Response, String name) {
    Map<String,String> cookies = Response.getCookies();

    assertTrue(cookies.containsKey(name), "Response doesn't have cookie with name " + name);
    return cookies.get(name);
    }
    protected String getStringFromJson(Response Response,String name) {
        return Response.jsonPath().getString("id");
    }
    protected String getStringJson(Response Response,String name ){
        return Response.jsonPath().getString("user_id");
    }
    protected int getIntFromJson(Response Response,String name) {
        Response.then().assertThat().body("$", hasKey(name));
        return Response.jsonPath().getInt(name);
    }
}