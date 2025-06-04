import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;


import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import io.restassured.http.Headers;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ex6Test {
    @Test
    public void  NewTest(){

        Response response = RestAssured
                .given()
                .redirects()
                .follow(true)
                .when()
                .get("https://playground.learnqa.ru/api/long_redirect")
                .andReturn();
        String location = response.getHeader("x-host");
        System.out.println(location);
    }
}
