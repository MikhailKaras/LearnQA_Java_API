import io.restassured.RestAssured;



import io.restassured.response.Response;
import org.junit.jupiter.api.Test;


public class Ex7Test {
    @Test
    public void  NewTest(){
        int code = 200;
        int x = 0;
        String url = "https://playground.learnqa.ru/api/long_redirect";
        while(x != code ) {
            Response response = RestAssured
                    .given()
                    .redirects()
                    .follow(false)
                    .when()
                    .get(url)
                    .andReturn();
            int StatusCode = response.getStatusCode();
            x = StatusCode;
            System.out.println("Статус код: " + x);
            if(StatusCode!= code){
                String location = response.getHeader("Location");
                System.out.println("Location: " + location);
                url = location;
            }
        }

    }
}
