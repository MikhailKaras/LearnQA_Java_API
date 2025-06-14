import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.Map;
import io.restassured.http.Headers;

import static org.junit.jupiter.api.Assertions.*;

public class Ex11Test {
    @Test
    public void Ex11Test()
    {
        Response response = RestAssured
                .given()
                .get("https://playground.learnqa.ru/api/homework_cookie")
                .andReturn();
        Map<String, String> responseCookies = response.getCookies();
        System.out.println(responseCookies);
        String name = response.getCookie("HomeWork");
        System.out.println(name);
        if(responseCookies.isEmpty()) {
            System.out.println("Cookies is empty");
        } else {
            assertTrue(responseCookies.containsKey("HomeWork"),"Cookie doesn't have that key");
            assertFalse(name.isEmpty(), "Cookie is empty" );
            assertNotNull(name,"Cookie must exist");
        }


    }

}
