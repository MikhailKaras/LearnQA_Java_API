package lib;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.response.Response;
import io.qameta.allure.restassured.AllureRestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class ApiCoreRequests {


    @Step("Make an user with incorrect field ")
    public Response createUserWithIncorField( Map<String,String> userData) {
        return given()
                .body(userData)
                .post("user/")
                .andReturn();
    }
    @Step("Make an user")
    public Response createUser( Map<String,String> userData) {
        return given()
                .body(userData)
                .post("user/")
                .andReturn();
    }
    @Step("Login an user")
    public Response loginUser( String email, String password) {
        Map<String,String> userData = new HashMap<>();
        userData.put("email",email);
        userData.put("password", password);
        return given()
                .body(userData)
                .post("user/login");
    }

    @Step("Get user info as user with auth token and cookie")
    public Response getUserInfo(String userId, String token, String cookie) {
        return  given()
                .header("x-csrf-token", token)
                .cookie("auth_sid", cookie)
                .get("user/" + userId);
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
    @Step("Send PUT-request to update user")
    public Response getResponseEditUser(String url, Map<String,String> editData, String token, String cookie)
    {
        return given()
                .body(editData)
                .header(new Header("x-csrf-token", token))
                .cookie("auth_sid", cookie)
                .put(url)
                .andReturn();
    }
    @Step("Send PUT-request to update user")
    public Response getResponseEditUser(String url, Map<String,String> editData)
    {
        return given()
                .body(editData)
                .put(url)
                .andReturn();
    }
    @Step("Send DELETE-request to delete user")
    public Response getResponseDeleteUser(String url, String token, String cookie) {
        return given()
                .header("x-csrf-token", token)
                .cookie("auth_sid", cookie)
                .delete(url)
                .andReturn();
    }

}
