package tests;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class UserEditTest extends BaseTestCase {
    @Test
    public void testEditJustCreatedTest()
    {
    //GENERATE USER
        Map<String,String> userData = DataGenerator.getRegistrationData();

        JsonPath responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .jsonPath();
        String userId = responseCreateAuth.getString("id");

        //LOGIN
        Map<String,String> authData = new HashMap<>();
        authData.put("email",userData.get("email"));
        authData.put("password",userData.get("password"));
        Response responseGetAuth = RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();
        //EDIT
        String newName = "Changed Name";
        Map<String,String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = RestAssured
                .given()
                .header("x-csrf-token", this.getHeader(responseGetAuth,"x-csrf-token"))
                .cookie("auth_sid", this.getCookie(responseGetAuth,"auth_sid"))
                .body(editData)
                .put("https://playground.learnqa.ru/api/user/" + userId)
                .andReturn();

        //GET
         Response responseUserData = RestAssured
                 .given()
                 .header("x-csrf-token",this.getHeader(responseGetAuth, "x-csrf-token"))
                 .cookie("auth_sid", this.getCookie(responseGetAuth,"auth_sid"))
                 .get("https://playground.learnqa.ru/api/user/" + userId)
                 .andReturn();

        Assertions.assertJsonByName(responseUserData,"firstName", newName);
    }
    String userid;
    private final lib.ApiCoreRequests ApiCoreRequests = new ApiCoreRequests();
    @Test
    public void testNegativeEditUserBeingNotAuthorized()
    {
        Map<String,String> userData1 = DataGenerator.getRegistrationData();
        Response responseCreateAuth1 = ApiCoreRequests.createUser(userData1);
        this.userid = this.getStringFromJson(responseCreateAuth1,"user_id");


        String newName = "John";
        Map<String,String> editData = new HashMap<>();
        editData.put("firstName", newName);
        Response responseEditSecondUser = ApiCoreRequests.getResponseEditUser("https://playground.learnqa.ru/api/user/"+userid,editData);
        Assertions.assertResponseCodeEquals(responseEditSecondUser,400);
        System.out.println(responseEditSecondUser.prettyPrint());
    }

    @Test
    public void testNegativeEditUserWhenAuthorizedAsAnother()
    {
        //create1
        Map<String,String> userData1 = DataGenerator.getRegistrationData();
        Response responseCreateAuth1 = ApiCoreRequests.createUser(userData1);


        //create2
        Map<String,String> userData2 = DataGenerator.getRegistrationData();
        Response responseCreateAuth2 = ApiCoreRequests.createUser(userData2);
        this.userid = this.getStringFromJson(responseCreateAuth2,"user_id");


        //Login
        Response responseLoginFirstUser = ApiCoreRequests.loginUser(userData1.get("email"),userData1.get("password"));
        String token = this.getHeader(responseLoginFirstUser, "x-csrf-token");
        String cookie = this.getCookie(responseLoginFirstUser, "auth_sid");

        //TryToEditSecondUserWhenUserLoggedInAsFirstUser
        String newName = "John";
        Map<String,String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditSecondUser = ApiCoreRequests.getResponseEditUser("https://playground.learnqa.ru/api/user/"+userid,editData,token,cookie);
        Assertions.assertResponseCodeEquals(responseEditSecondUser,400);
    }

    @Test
    public void testEditUserWithIncorrectEmail()
    {
        //Create
        Map<String,String> userData1 = DataGenerator.getRegistrationData();
        Response responseCreateAuth1 = ApiCoreRequests.createUser(userData1);
        this.userid = this.getStringFromJson(responseCreateAuth1,"user_id");

        //Login
        Response responseLoginFirstUser = ApiCoreRequests.loginUser(userData1.get("email"),userData1.get("password"));
        String token = this.getHeader(responseLoginFirstUser, "x-csrf-token");
        String cookie = this.getCookie(responseLoginFirstUser, "auth_sid");

        Map<String, String> editData = new HashMap<>();
        editData.put("email", DataGenerator.getRandomIncorrectEmail());
        //Put
        Response responseEditUser = ApiCoreRequests.getResponseEditUser("https://playground.learnqa.ru/api/user/"+userid, editData,token,cookie);
        Assertions.assertResponseCodeEquals(responseEditUser,400);
        System.out.println(responseEditUser.prettyPrint());

    }

    @Test
    public void testEditUserWithIncorrectFirstName()
    {
        //Create
        Map<String,String> userData1 = DataGenerator.getRegistrationData();
        Response responseCreateAuth1 = ApiCoreRequests.createUser(userData1);
        this.userid = this.getStringFromJson(responseCreateAuth1,"user_id");

        //Login
        Response responseLoginFirstUser = ApiCoreRequests.loginUser(userData1.get("email"),userData1.get("password"));
        String token = this.getHeader(responseLoginFirstUser, "x-csrf-token");
        String cookie = this.getCookie(responseLoginFirstUser, "auth_sid");

        //Put
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", DataGenerator.createShortUserName());
        Response responseEditUser = ApiCoreRequests.getResponseEditUser("https://playground.learnqa.ru/api/user/"+userid, editData,token,cookie);
        Assertions.assertResponseCodeEquals(responseEditUser,400);
        System.out.println(responseEditUser.prettyPrint());
    }
}
