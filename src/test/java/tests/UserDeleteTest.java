package tests;

import io.qameta.allure.*;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;
@Epic("Delete cases")
@Feature("Delete user")
public class UserDeleteTest extends BaseTestCase{
    private final lib.ApiCoreRequests ApiCoreRequests = new ApiCoreRequests();
    @Test
    @Description("This test checks the impossibility of deleting user with id 2")
    @Story("Delete user with id 2")
    @DisplayName("Test positive delete user 2")
    @Tag("api_dev")
    @Severity(SeverityLevel.NORMAL)
    @Owner("Mikhail Karas")
    public void testDeleteUserId2()
    {


        Map<String,String> authData = new HashMap<>();
        authData.put("email","vinkotov@example.com");
        authData.put("password","1234");
        Response responseAuthUserId2 = ApiCoreRequests.loginUser(authData.get("email"),authData.get("password"));
        Assertions.assertResponseCodeEquals(responseAuthUserId2, 200);
        String token = this.getHeader(responseAuthUserId2, "x-csrf-token");
        String cookie = this.getCookie(responseAuthUserId2, "auth_sid");
        String userId = this.getStringFromJson(responseAuthUserId2,"id");
        System.out.println("User ID: " + userId);
        String deleteUrl = "user/" + userid;

        Response responseDeleteUserId2 = ApiCoreRequests.getResponseDeleteUser(deleteUrl ,token,cookie);

        System.out.println(responseDeleteUserId2.prettyPrint());
    }
    @Test
    @Description("This test delete user what was created")
    @Story("Delete an user")
    @DisplayName("Test positive delete user")
    @Tag("api_dev")
    @Severity(SeverityLevel.NORMAL)
    @Owner("Mikhail Karas")
    public void testPositiveDeleteUser()

    {
        System.out.println("Current baseURI: " + RestAssured.baseURI);
        Map<String,String> userData = DataGenerator.getRegistrationData();
        Response responseCreateAuth = ApiCoreRequests.createUser(userData);


        Response responseLoginUser = ApiCoreRequests.loginUser(userData.get("email"),userData.get("password"));
        System.out.println("Login response: " + responseLoginUser.asString());
        String token = this.getHeader(responseLoginUser, "x-csrf-token");
        String cookie = this.getCookie(responseLoginUser, "auth_sid");
        String userId = this.getStringJson(responseLoginUser,"user_id");

        String deleteUrl = "user/"+ userId ;
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
    @Description("This test checks that user cant delete another one")
    @Story("Delete an other user by another user")
    @DisplayName("Test negative delete user")
    @Tag("api_dev")
    @Severity(SeverityLevel.NORMAL)
    @Owner("Mikhail Karas")
    public void testNegativeDeleteUser()
    {
        System.out.println("Current baseURI: " + RestAssured.baseURI);
        Map<String,String> userData1 = DataGenerator.getRegistrationData();
        Response responseCreateAuth1 = ApiCoreRequests.createUser(userData1);

        Map<String,String> userData2 = DataGenerator.getRegistrationData();
        Response responseCreateAuth2 = ApiCoreRequests.createUser(userData2);
        String userId = this.getStringJson(responseCreateAuth2,"user_id");

        Response responseLoginFirstUser = ApiCoreRequests.loginUser(userData1.get("email"),userData1.get("password"));
        String token = this.getHeader(responseLoginFirstUser, "x-csrf-token");
        String cookie = this.getCookie(responseLoginFirstUser, "auth_sid");

        String deleteUrl = "user/"+ userId ;
        Response responseDeleteUser2 = ApiCoreRequests.getResponseDeleteUser(deleteUrl,token,cookie);
        System.out.println("DELETE status code: " + responseDeleteUser2.getStatusCode());
        System.out.println(responseDeleteUser2.prettyPrint());

    }

}
