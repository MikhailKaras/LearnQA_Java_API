import io.restassured.RestAssured;

import java.util.*;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class Ex9Test {
    @Test
    public void PasswordTest()  {
        List<String> passwords = new ArrayList<>();

        passwords.add("123456");
        passwords.add("123456789");
        passwords.add("qwerty");
        passwords.add("password");
        passwords.add("1234567");
        passwords.add("12345678");
        passwords.add("12345");
        passwords.add("iloveyou");
        passwords.add("111111");
        passwords.add("123123");
        passwords.add("abc123");
        passwords.add("qwerty123");
        passwords.add("1q2w3e4r");
        passwords.add("admin");
        passwords.add("qwertyuiop");
        passwords.add("654321");
        passwords.add("555555");
        passwords.add("lovely");
        passwords.add("7777777");
        passwords.add("welcome");
        passwords.add("888888");
        passwords.add("princess");
        passwords.add("dragon");
        passwords.add("password1");
        passwords.add("123qwe");

        String login = "super_admin";
        int index = 0;
        String trueAnswer = "You are authorized";

        while (index < passwords.size()) {
            String currentPassword = passwords.get(index);
            Map<String, Object> data = new HashMap<>();
            data.put("login", login);
            data.put("password", currentPassword);

            Response response = RestAssured
                    .given()
                    .body(data)
                    .post("https://playground.learnqa.ru/ajax/api/get_secret_password_homework")
                    .andReturn();

            String cookie = response.getCookie("auth_cookie");
            System.out.println("Проверяем пароль: " + currentPassword);
            System.out.println("Cookie: " + cookie);


                Response checkResponse = RestAssured
                        .given()
                        .cookie("auth_cookie", cookie)
                        .get("https://playground.learnqa.ru/ajax/api/check_auth_cookie")
                        .andReturn();

                String answer = checkResponse.getBody().asString();
                System.out.println("Ответ: " + answer);

                if (trueAnswer.equals(answer)) {
                    System.out.println("Авторизация успешна!");
                    System.out.println("Верный пароль: " + currentPassword);
                    break;
                }


            index++;

        }
    }
}
