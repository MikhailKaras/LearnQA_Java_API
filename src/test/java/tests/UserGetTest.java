package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class UserGetTest extends BaseTestCase {
    @Test
    public void testGetUserDataNotAuth(){
        Response responseUserData = RestAssured
                .get("https://playground.learnqa.ru/api/user/2")
                .andReturn();
        Assertions.assertJsonHasField(responseUserData,"username");
        Assertions.assertJsonHasNotField(responseUserData,"firstName");
        Assertions.assertJsonHasNotField(responseUserData,"lastName");
        Assertions.assertJsonHasNotField(responseUserData,"email");
    }
    @Test
    public void testGetUserDetailsAuthAsSameUser()
    {
        Map<String,String> authData = new HashMap<>();
        authData.put("email","vinkotov@example.com");
        authData.put("password","1234");

        Response responseGetAuth = RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();
        String header = this.getHeader(responseGetAuth, "x-csrf-token");
        String cookie = this.getCookie(responseGetAuth,"auth_sid");

        Response responseUserData = RestAssured
                .given()
                .header("x-csrf-token", header )
                .cookie("auth_sid", cookie)
                .get("https://playground.learnqa.ru/api/user/2")
                .andReturn();

        String[] expectedFields = {"username","firstName","lastName","email"};
        Assertions.assetJsonHasFields(responseUserData,expectedFields);
    }

    String userIdonAuth;
    String token;
    String userIdonAuth2;
    String cookie;
    private final lib.ApiCoreRequests ApiCoreRequests = new ApiCoreRequests();
    @Test
    public void testLoginUser()
    {
        Map<String,String> authData = DataGenerator.getRegistrationData();
        Response responseCreateFirstUser = ApiCoreRequests.createUser(authData);
        this.userIdonAuth = this.getStringFromJson(responseCreateFirstUser,"user_id");
        System.out.println(responseCreateFirstUser.getStatusCode());
        System.out.println(userIdonAuth);


        Map<String,String> authData2 = DataGenerator.getRegistrationData();
        Response responseCreateSecondUser = ApiCoreRequests.createUser(authData2);
        this.userIdonAuth2 = this.getStringFromJson(responseCreateSecondUser,"user_id");
        System.out.println(responseCreateSecondUser.getStatusCode());
        System.out.println(userIdonAuth2);


        Response responseLoginFirstUser = ApiCoreRequests.loginUser(authData.get("email"),authData.get("password"));
        Assertions.assertResponseCodeEquals(responseLoginFirstUser, 200);
        String token = this.getHeader(responseLoginFirstUser,"x-csrf-token");
        String cookie = this.getCookie(responseLoginFirstUser,"auth_sid");

        Response responseGetInfoUser = ApiCoreRequests.getUserInfo(userIdonAuth2,token,cookie);
        Assertions.assertJsonHasField(responseGetInfoUser,"username");
        Assertions.assertJsonHasNotField(responseGetInfoUser,"firstName");
        Assertions.assertJsonHasNotField(responseGetInfoUser,"lastName");
        Assertions.assertJsonHasNotField(responseGetInfoUser,"email");

    }
}
