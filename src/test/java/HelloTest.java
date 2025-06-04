import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;


import org.junit.jupiter.api.Test;



import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HelloTest {
    @Test
    public void TestRestAssured(){
        Map<String,Object> params = new HashMap<>();
        params.put("messages","message");

        JsonPath response = RestAssured
                .given()
                .queryParams(params)
                .get("https://playground.learnqa.ru/api/get_json_homework")
                .jsonPath();
        response.prettyPrint();
        List<Map<String,String>> param2 = response.get("messages");
        Map<String,String> param3 = param2.get(1);
        String secondMessage = param3.get("message");
        System.out.println(secondMessage);

    }
}
