package tests;

import io.qameta.allure.*;
import io.restassured.RestAssured;
import io.restassured.internal.RestAssuredResponseImpl;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lib.BaseTestCase;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import lib.Assertions;
import java.util.HashMap;
import java.util.Map;

import lib.ApiCoreRequests;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@Epic("Authorization cases")
@Feature("Authorization")
public class UserAuthTest  extends BaseTestCase {

    String cookie;
    String header;
    int userIdonAuth;
    private final ApiCoreRequests ApiCoreRequests = new ApiCoreRequests();
    @BeforeEach
    public void loginUser() {
        System.out.println("Current baseURI: " + RestAssured.baseURI);
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");
        Response responseGetAuth = ApiCoreRequests
                .makePostRequest("login/", authData);
        this.cookie = this.getCookie(responseGetAuth,"auth_sid");
        this.header = this.getHeader(responseGetAuth,"x-csrf-token");
        this.userIdonAuth = this.getIntFromJson(responseGetAuth,"user_id");
    }

    @Test
    @Description("This test successfully authorize user by email and password")
    @Story("Login with valid data")
    @DisplayName("Test positive auth user")
    @Tag("api_dev")
    @Severity(SeverityLevel.NORMAL)
    @Owner("Mikhail Karas")
    public void testAuthUser() {
        System.out.println("Current baseURI: " + RestAssured.baseURI);
        Response responseCheckAuth = ApiCoreRequests
                .makeGetRequest("auth/",
                        this.header,
                        this.cookie);
        Assertions.assertJsonByName(responseCheckAuth, "user_id", this.userIdonAuth);
    }
    @Description("This test checks authorization status without sending auth cookie or token")
    @Story("Login without sending data")
    @DisplayName("Test negative auth user")
    @Tag("api_dev")
    @Severity(SeverityLevel.NORMAL)
    @Owner("Mikhail Karas")
    @ParameterizedTest
    @ValueSource(strings = {"cookie","headers"})
    public void testNegativeAuthUser(String condition){



        if(condition.equals("cookie")){
            Response responseForCheck = ApiCoreRequests.makeGetRequestWithCookie("auth/",
                    this.cookie);
            Assertions.assertJsonByName(responseForCheck,"user_id",0);
        }else if(condition.equals("headers"))
        {
            Response responseForCheck = ApiCoreRequests.makeGetRequestWithToken("auth/",
                    this.header);
            Assertions.assertJsonByName(responseForCheck,"user_id", 0);
        }else {
            throw new IllegalArgumentException("Condition value is not known: " + condition);
        }


    }
}




