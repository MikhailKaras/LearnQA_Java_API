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

public class UserRegisterTest extends BaseTestCase {
    @Test
    public void testCreateUserWithExistingEmail()
    {
        String email = "vinkotov@example.com";

        Map<String,String> userData = new HashMap<>();
        userData.put("email",email);
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .andReturn();

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "Users with email '" + email + "' already exists");
    }
    @Test
    public void testCreateUserSuccessfully()
    {
        String email = DataGenerator.getRandomEmail();

        Map<String,String> userData =  DataGenerator.getRegistrationData();

        Response responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .andReturn();

        Assertions.assertResponseCodeEquals(responseCreateAuth, 200);
        Assertions.assertJsonHasField(responseCreateAuth,"id");

    }
    private final lib.ApiCoreRequests ApiCoreRequests = new ApiCoreRequests();
    @Test
    @Description("This test checks reaction of server when user tries to create an account with incorrect email")
    @DisplayName("Test negative create user")
    public void testCreateUserWithIncorrectEmail()
    {
        //no@
        Map<String,String> userData = DataGenerator.getRegistrationWithIncorrectMailData();

        Response responseCreateAuth = ApiCoreRequests.createUserWithIncorField(userData);

        Assertions.assertJsonHasNotField(responseCreateAuth,"email");
        Assertions.assertResponseCodeEquals(responseCreateAuth,400);
    }

    @Test
    @Description("This test checks reaction of server when user tries to create an account with short username")
    @DisplayName("Test negative auth user")
    public void testCreateUserWithShortLogin()
    {
        //shortUserName
        Map<String,String> shortUserData = DataGenerator.getRegistrationDataWithShortUserName();

        Response responseCreateUserWithShortUserName = ApiCoreRequests.createUserWithIncorField(shortUserData);

        Assertions.assertResponseCodeEquals(responseCreateUserWithShortUserName,400);
        Assertions.assertJsonHasNotField(responseCreateUserWithShortUserName,"username");
        Assertions.assertResponseTextEquals(responseCreateUserWithShortUserName,"The value of 'username' field is too short");
    }

    @Test
    @Description("This test checks reaction of server when user tries to create an account with long username")
    @DisplayName("Test negative auth user")
    public void testCreateUserWithLongLogin()
    {   //LongLogin
        Map<String,String> longUserData = DataGenerator.getRegistrationDataWithLongUserName();

        Response responseCreateUserWithLongUserName = ApiCoreRequests.createUserWithIncorField(longUserData);

        Assertions.assertResponseCodeEquals(responseCreateUserWithLongUserName,400);
        Assertions.assertJsonHasNotField(responseCreateUserWithLongUserName,"username");
        Assertions.assertResponseTextEquals(responseCreateUserWithLongUserName,"The value of 'username' field is too long");
    }
    @Description("This test checks reaction of server when user tries to create an account with one null every field")
    @DisplayName("Test negative auth user")
    @ParameterizedTest
    @ValueSource(strings = {"email","password","username","firstName","lastName"})
    public void testCreateUserWithoutOneOfNecessaryField(String missedField)
    {
        //NullFields
        Map<String,String> userData = DataGenerator.getRegistrationData();

        userData.remove(missedField);
        Response responseCreateUserWithoutOneField = ApiCoreRequests.createUserWithIncorField(userData);


        Assertions.assertResponseCodeEquals(responseCreateUserWithoutOneField,400);
        Assertions.assertResponseTextEquals(responseCreateUserWithoutOneField,"The following required params are missed: "+ missedField);
    }

}
