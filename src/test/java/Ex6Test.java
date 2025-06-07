import io.restassured.RestAssured;



import io.restassured.response.Response;
import org.junit.jupiter.api.Test;


public class Ex6Test {
    @Test
    public void  NewTest(){

        Response response = RestAssured
                .given()
                .redirects()
                .follow(false)
                .when()
                .get("https://playground.learnqa.ru/api/long_redirect")
                .andReturn();
        String location = response.getHeader("Location");
        System.out.println(location);
    }
}
