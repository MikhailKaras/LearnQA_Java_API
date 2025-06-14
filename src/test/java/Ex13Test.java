import io.restassured.response.Response;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import io.restassured.RestAssured;


import java.util.ArrayList;

import java.util.List;


public class Ex13Test {
    @ParameterizedTest
    @ValueSource(strings =
            {"Mozilla/5.0 (Linux; U; Android 4.0.2; en-us; Galaxy Nexus Build/ICL53F) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30",
            "Mozilla/5.0 (iPad; CPU OS 13_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/91.0.4472.77 Mobile/15E148 Safari/604.1",
                "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36 Edg/91.0.100.0",
                "Mozilla/5.0 (iPad; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1"}

    )

    public void testUserAgent(String userAgent)

    {
        String expectedPlatform = null;
        String expectedBrowser = null ;
        String expectedDevice = null;
        if (userAgent.contains("Android")) {
            expectedPlatform = "Mobile";
            expectedBrowser = "No";
            expectedDevice = "Android";
        } else if (userAgent.contains("CriOS")) {
            expectedPlatform = "Mobile";
            expectedBrowser = "Chrome";
            expectedDevice = "iOS";
        } else if (userAgent.contains("Edg")) {
            expectedPlatform = "Web";
            expectedBrowser = "Chrome";
            expectedDevice = "No";
        } else if (userAgent.contains("iPhone") || userAgent.contains("iPad")) {
            expectedPlatform = "Mobile";
            expectedBrowser = "No";
            expectedDevice = "iPhone";
        } else if (userAgent.contains("Googlebot")) {
            expectedPlatform = "Googlebot";
            expectedBrowser = "Unknown";
            expectedDevice = "Unknown";
        }

        Response response = RestAssured
                .given()
                .header("User-Agent", userAgent)
                .get("https://playground.learnqa.ru/ajax/api/user_agent_check")
                .andReturn();
        String platform = response.jsonPath().getString("platform");
        String device = response.jsonPath().getString("device");
        String browser = response.jsonPath().getString("browser");

        boolean Haserror = false;
        List<String> Errors = new ArrayList<>();
        if (!expectedPlatform.equals(platform)) {
            Errors.add("[Error] platform: expected '" + expectedPlatform + "', received '" + platform + "'");
            Haserror = true;
        }

        if (!expectedBrowser.equals(browser)) {
            Errors.add("[Error] browser: expected '" + expectedBrowser + "', received  '" + browser + "'");
            Haserror = true;
        }


        if (!expectedDevice.equals(device)) {
            Errors.add("[Error] device: expected '" + expectedDevice + "', received  '" + device + "'");
            Haserror = true;
        }

        if(Haserror == true) {
            System.out.println("Errors for User Agent:" + userAgent);
            for (String error : Errors) {
                System.out.println(error);
            }
        }


    }

}
