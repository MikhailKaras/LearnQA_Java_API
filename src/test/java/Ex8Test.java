import io.restassured.RestAssured;


import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;

public class Ex8Test {
    @Test
    public void TokenTest() throws InterruptedException {
        JsonPath response = RestAssured
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();
        response.prettyPrint();
        int seconds = response.get("seconds");
        System.out.println(seconds);
        String token = response.getString("token");
        Map<String,Object > params = new HashMap<>();
        params.put("token",token);
        Response response1 = RestAssured
                .given()
                .queryParams(params)
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .andReturn();
        response1.prettyPrint();
        Thread.sleep(seconds*1000);
        Response response2 = RestAssured
                .given()
                .queryParams(params)
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .andReturn();
        response2.prettyPrint();
    }
}
