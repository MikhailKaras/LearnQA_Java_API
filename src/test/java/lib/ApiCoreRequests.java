package lib;

import io.qameta.allure.Step;
import io.restassured.http.Header;
import io.restassured.response.Response;
import io.qameta.allure.restassured.AllureRestAssured;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class ApiCoreRequests {

    @Step("Make an user with incorrect field ")
    public Response createUserWithIncorField( Map<String,String> userData) {
        return given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .andReturn();
    }
    @Step("Make an user")
    public Response createUser( Map<String,String> userData) {
        return given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .andReturn();
    }
    @Step("Login an user")
    public Response loginUser( String email, String password) {
        Map<String,String> userData = new HashMap<>();
        userData.put("email",email);
        userData.put("password", password);
        return given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/login");
    }
    @Step("Get user info as user with auth token and cookie")
    public Response getUserInfo(String userId, String token, String cookie) {
        return  given()
                .header("x-csrf-token", token)
                .cookie("auth_sid", cookie)
                .get("https://playground.learnqa.ru/api/user/" + userId);
    }

    @Step("Make a Get-Request with token and auth cookie")
    public Response makeGetRequest(String url, String token, String cookie)
    {
        return given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", token))
                .cookie("auth_sid", cookie)
                .get(url)
                .andReturn();
    }

    @Step("Make a Get-Request with auth cookie")
    public Response makeGetRequestWithCookie(String url, String cookie)
    {
        return given()
                .filter(new AllureRestAssured())
                .cookie("auth_sid", cookie)
                .get(url)
                .andReturn();
    }

    @Step("Make a Get-Request with token")
    public Response makeGetRequestWithToken(String url, String token)
    {
        return given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", token))
                .get(url)
                .andReturn();
    }

    @Step("Make a Post-Request with token")
    public Response makePostRequest(String url, Map<String,String> authData)
    {
        return given()
                .filter(new AllureRestAssured())
                .body(authData)
                .post(url)
                .andReturn();
    }
}
