import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import io.restassured.http.Headers;


import static org.junit.jupiter.api.Assertions.*;

public class Ex12Test {
    @Test
    public void Ex12Test() {
        Response response = RestAssured
                .given()
                .get("https://playground.learnqa.ru/api/homework_header")
                .andReturn();
        Headers responseHeaders = response.getHeaders();
        System.out.println(responseHeaders);
        String header = response.getHeader("x-secret-homework-header");
        System.out.println(header);

            assertFalse(header.isEmpty(),"The header's value must be not null");
            assertNotNull(header,"Header must exist");

    }
}
