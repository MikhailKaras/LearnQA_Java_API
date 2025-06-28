package tests;

import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

public class UserDeleteTest extends BaseTestCase{
    private final lib.ApiCoreRequests ApiCoreRequests = new ApiCoreRequests();
    @Test
    public void testDeleteUserId2()
    {
        String deleteUrl = "https://playground.learnqa.ru/api/user/ ";
        Map<String,String> authData = new HashMap<>();
        authData.put("email","vinkotov@example.com");
        authData.put("password","1234");
        Response responseAuthUserId2 = ApiCoreRequests.loginUser(authData.get("email"),authData.get("password"));
        Assertions.assertResponseCodeEquals(responseAuthUserId2, 200);
        String token = this.getHeader(responseAuthUserId2, "x-csrf-token");
        String cookie = this.getCookie(responseAuthUserId2, "auth_sid");
        String userId = this.getStringFromJson(responseAuthUserId2,"id");
        System.out.println("User ID: " + userId);


        Response responseDeleteUserId2 = ApiCoreRequests.getResponseDeleteUser(deleteUrl + userId ,token,cookie);

        System.out.println(responseDeleteUserId2.prettyPrint());
    }
    @Test
    public void testPositiveDeleteUser()
    {
        Map<String,String> userData = DataGenerator.getRegistrationData();
        Response responseCreateAuth = ApiCoreRequests.createUser(userData);


        Response responseLoginUser = ApiCoreRequests.loginUser(userData.get("email"),userData.get("password"));
        System.out.println("Login response: " + responseLoginUser.asString());
        String token = this.getHeader(responseLoginUser, "x-csrf-token");
        String cookie = this.getCookie(responseLoginUser, "auth_sid");
        String userId = this.getStringJson(responseLoginUser,"user_id");

        String deleteUrl = "https://playground.learnqa.ru/api/user/"+ userId ;
        System.out.println(deleteUrl);
        Response responseDeleteUser = ApiCoreRequests.getResponseDeleteUser(deleteUrl,token,cookie);
        System.out.println("DELETE status code: " + responseDeleteUser.getStatusCode());



        Response getInfoAfterDelete = ApiCoreRequests.getUserInfo(userId,token,cookie);
        Assertions.assertJsonHasNotField(getInfoAfterDelete,"firstName");
        Assertions.assertJsonHasNotField(getInfoAfterDelete,"lastName");
        Assertions.assertJsonHasNotField(getInfoAfterDelete,"email");
        Assertions.assertResponseCodeEquals(getInfoAfterDelete,404);
    }

    String userid;
    @Test
    public void testNegativeDeleteUser()
    {
        Map<String,String> userData1 = DataGenerator.getRegistrationData();
        Response responseCreateAuth1 = ApiCoreRequests.createUser(userData1);

        Map<String,String> userData2 = DataGenerator.getRegistrationData();
        Response responseCreateAuth2 = ApiCoreRequests.createUser(userData2);
        this.userid = this.getStringFromJson(responseCreateAuth2,"user_id");

        Response responseLoginFirstUser = ApiCoreRequests.loginUser(userData1.get("email"),userData1.get("password"));
        String token = this.getHeader(responseLoginFirstUser, "x-csrf-token");
        String cookie = this.getCookie(responseLoginFirstUser, "auth_sid");

        String deleteUrl = "https://playground.learnqa.ru/api/user/"+ userid ;
        Response responseDeleteUser2 = ApiCoreRequests.getResponseDeleteUser(deleteUrl,token,cookie);
        System.out.println("DELETE status code: " + responseDeleteUser2.getStatusCode());
        System.out.println(responseDeleteUser2.prettyPrint());

    }

}
