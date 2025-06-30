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
@Epic("Register cases")
@Feature("Register")
public class UserRegisterTest extends BaseTestCase {

    @Test
    @Description("This test checks the impossibility of creating an user with existing email")
    @Story("Create user with existing email")
    @DisplayName("Test negative create user")
    @Tag("api_dev")
    @Severity(SeverityLevel.NORMAL)
    @Owner("Mikhail Karas")
    public void testCreateUserWithExistingEmail()
    {
        String email = "vinkotov@example.com";
        System.out.println("Current baseURI: " + RestAssured.baseURI);
        Map<String,String> userData = new HashMap<>();
        userData.put("email",email);
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post("user/")
                .andReturn();

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "Users with email '" + email + "' already exists");
    }
    @Test
    @Description("This test checks creation of new user")
    @Story("Create user")
    @DisplayName("Test positive create user")
    @Tag("api_dev")
    @Severity(SeverityLevel.NORMAL)
    @Owner("Mikhail Karas")
    public void testCreateUserSuccessfully()
    {
        String email = DataGenerator.getRandomEmail();
        System.out.println("Current baseURI: " + RestAssured.baseURI);
        Map<String,String> userData =  DataGenerator.getRegistrationData();

        Response responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post("user/")
                .andReturn();

        Assertions.assertResponseCodeEquals(responseCreateAuth, 200);
        Assertions.assertJsonHasField(responseCreateAuth,"id");

    }
    private final lib.ApiCoreRequests ApiCoreRequests = new ApiCoreRequests();
    @Test
    @Description("This test checks the impossibility of creation an user with incorrect email")
    @Story("Create user with incorrect email")
    @DisplayName("Test negative create user")
    @Tag("api_dev")
    @Severity(SeverityLevel.NORMAL)
    @Owner("Mikhail Karas")
    public void testCreateUserWithIncorrectEmail()
    {
        //no@
        System.out.println("Current baseURI: " + RestAssured.baseURI);
        Map<String,String> userData = DataGenerator.getRegistrationWithIncorrectMailData();

        Response responseCreateAuth = ApiCoreRequests.createUserWithIncorField(userData);

        Assertions.assertJsonHasNotField(responseCreateAuth,"email");
        Assertions.assertResponseCodeEquals(responseCreateAuth,400);
    }

    @Test
    @Description("This test checks the impossibility of creation an user with short username")
    @Story("Create user with short username")
    @DisplayName("Test negative auth user")
    @Tag("api_dev")
    @Severity(SeverityLevel.NORMAL)
    @Owner("Mikhail Karas")
    public void testCreateUserWithShortLogin()
    {
        //shortUserName
        System.out.println("Current baseURI: " + RestAssured.baseURI);
        Map<String,String> shortUserData = DataGenerator.getRegistrationDataWithShortUserName();

        Response responseCreateUserWithShortUserName = ApiCoreRequests.createUserWithIncorField(shortUserData);

        Assertions.assertResponseCodeEquals(responseCreateUserWithShortUserName,400);
        Assertions.assertJsonHasNotField(responseCreateUserWithShortUserName,"username");
        Assertions.assertResponseTextEquals(responseCreateUserWithShortUserName,"The value of 'username' field is too short");
    }

    @Test
    @Description("This test checks the impossibility of creation user with long username")
    @Story("Create user with long username")
    @DisplayName("Test negative auth user")
    @Tag("api_dev")
    @Severity(SeverityLevel.NORMAL)
    @Owner("Mikhail Karas")
    public void testCreateUserWithLongLogin()
    {   //LongLogin
        System.out.println("Current baseURI: " + RestAssured.baseURI);
        Map<String,String> longUserData = DataGenerator.getRegistrationDataWithLongUserName();

        Response responseCreateUserWithLongUserName = ApiCoreRequests.createUserWithIncorField(longUserData);

        Assertions.assertResponseCodeEquals(responseCreateUserWithLongUserName,400);
        Assertions.assertJsonHasNotField(responseCreateUserWithLongUserName,"username");
        Assertions.assertResponseTextEquals(responseCreateUserWithLongUserName,"The value of 'username' field is too long");
    }
    @Description("This test checks the impossibility of creation an account with one null every field")
    @Story("Create user with one null every field")
    @DisplayName("Test negative auth user")
    @Severity(SeverityLevel.NORMAL)
    @Tag("api_dev")
    @Owner("Mikhail Karas")
    @ParameterizedTest
    @ValueSource(strings = {"email","password","username","firstName","lastName"})
    public void testCreateUserWithoutOneOfNecessaryField(String missedField)
    {
        //NullFields
        System.out.println("Current baseURI: " + RestAssured.baseURI);
        Map<String,String> userData = DataGenerator.getRegistrationData();


        userData.remove(missedField);
        Response responseCreateUserWithoutOneField = ApiCoreRequests.createUserWithIncorField(userData);


        Assertions.assertResponseCodeEquals(responseCreateUserWithoutOneField,400);
        Assertions.assertResponseTextEquals(responseCreateUserWithoutOneField,"The following required params are missed: "+ missedField);
    }

}
