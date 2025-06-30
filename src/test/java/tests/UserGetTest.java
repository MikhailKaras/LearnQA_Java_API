package tests;

import io.qameta.allure.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
@Epic("Get user data cases")
@Feature("Getting users data")
public class UserGetTest extends BaseTestCase {
    @Test
    @Description("This test checks the possibility of getting user data that are not authorized")
    @Story("Get user data that are not authorized")
    @DisplayName("Test negative get user data")
    @Tag("api_dev")
    @Severity(SeverityLevel.NORMAL)
    @Owner("Mikhail Karas")
    public void testGetUserDataNotAuth(){
        Response responseUserData = RestAssured
                .get("user/2")
                .andReturn();
        Assertions.assertJsonHasField(responseUserData,"username");
        Assertions.assertJsonHasNotField(responseUserData,"firstName");
        Assertions.assertJsonHasNotField(responseUserData,"lastName");
        Assertions.assertJsonHasNotField(responseUserData,"email");
    }
    @Test
    @Description("This test checks the possibility of getting user data by the same user")
    @Story("Get user data by the same user")
    @DisplayName("Test positive get user data")
    @Tag("api_dev")
    @Severity(SeverityLevel.NORMAL)
    @Owner("Mikhail Karas")
    public void testGetUserDetailsAuthAsSameUser()
    {
        Map<String,String> authData = new HashMap<>();
        authData.put("email","vinkotov@example.com");
        authData.put("password","1234");

        Response responseGetAuth = RestAssured
                .given()
                .body(authData)
                .post("/login")
                .andReturn();
        String header = this.getHeader(responseGetAuth, "x-csrf-token");
        String cookie = this.getCookie(responseGetAuth,"auth_sid");

        Response responseUserData = RestAssured
                .given()
                .header("x-csrf-token", header )
                .cookie("auth_sid", cookie)
                .get("user/2")
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
    @Description("This test checks getting user data from a new created user")
    @Story("Get new user data by the same new user")
    @DisplayName("Test positive get user data")
    @Tag("api_dev")
    @Severity(SeverityLevel.NORMAL)
    @Owner("Mikhail Karas")
    public void testLoginUser()
    {
        Map<String,String> authData = DataGenerator.getRegistrationData();
        Response responseCreateFirstUser = ApiCoreRequests.createUser(authData);
        this.userIdonAuth = this.getStringFromJson(responseCreateFirstUser,"user_id");
        System.out.println(responseCreateFirstUser.getStatusCode());
        System.out.println(userIdonAuth);


        Map<String,String> authData2 = DataGenerator.getRegistrationData();
        Response responseCreateSecondUser = ApiCoreRequests.createUser(authData2);
        this.userIdonAuth2 = this.getStringJson(responseCreateSecondUser,"user_id");
        System.out.println(responseCreateSecondUser.getStatusCode());
        System.out.println(userIdonAuth2);


        Response responseLoginFirstUser = ApiCoreRequests.loginUser(authData.get("email"),authData.get("password"));
        Assertions.assertResponseCodeEquals(responseLoginFirstUser, 200);
        String token = this.getHeader(responseLoginFirstUser,"x-csrf-token");
        String cookie = this.getCookie(responseLoginFirstUser,"auth_sid");

        Response responseGetInfoUser = ApiCoreRequests.getUserInfo(userIdonAuth2,token,cookie);
        Assertions.assertResponseCodeEquals(responseGetInfoUser, 200);
        Assertions.assertJsonHasField(responseGetInfoUser,"username");
        Assertions.assertJsonHasNotField(responseGetInfoUser,"firstName");
        Assertions.assertJsonHasNotField(responseGetInfoUser,"lastName");
        Assertions.assertJsonHasNotField(responseGetInfoUser,"email");

    }
}
