package lib;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class DataGenerator {
    public static String getRandomEmail()
    {
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
        return "learnqa" + timestamp + "@example.com";
    }
    public static String getRandomIncorrectEmail()
    {
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
        return "learnqa" + timestamp + "example.com";
    }
    public static String createShortUserName()
    {
        return "a";
    }
    public static String createLongUserName()
    {
        int i = 0;
        String userName = "a";
        for(i =0;i<252;i++)
        {
            userName = userName + "a";
        }
        return userName;
    }

    public static Map<String,String> getRegistrationData()
    {
        Map<String,String> data = new HashMap<>();
        data.put("email", DataGenerator.getRandomEmail());
        data.put("password","123");
        data.put("username","learnqa");
        data.put("firstName","learnqa");
        data.put("lastName","learnqa");
        return data;
    }
    public static Map<String,String> getRegistrationDataWithShortUserName()
    {
        Map<String,String> data = new HashMap<>();
        data.put("email", DataGenerator.getRandomEmail());
        data.put("password","123");
        data.put("username",DataGenerator.createShortUserName());
        data.put("firstName","learnqa");
        data.put("lastName","learnqa");
        return data;
    }
    public static Map<String,String> getRegistrationDataWithLongUserName()
    {
        Map<String,String> data = new HashMap<>();
        data.put("email", DataGenerator.getRandomEmail());
        data.put("password","123");
        data.put("username",DataGenerator.createLongUserName());
        data.put("firstName","learnqa");
        data.put("lastName","learnqa");
        return data;
    }
    public static Map<String,String> getRegistrationWithIncorrectMailData()
    {
        Map<String,String> data = new HashMap<>();
        data.put("email", DataGenerator.getRandomIncorrectEmail());
        data.put("password","123");
        data.put("username","learnqa");
        data.put("firstName","learnqa");
        data.put("lastName","learnqa");
        return data;
    }

    public static Map<String,String> getRegistrationData(Map<String,String> nonDefaultValues)
    {
        Map<String,String> defaultValues = DataGenerator.getRegistrationData();

        Map<String,String> userData = new HashMap<>();
        String[] keys = {"email","password", "username", "firstName", "lastName"};
        for (String key : keys)
        {
            if (nonDefaultValues.containsKey(key))
            {
                userData.put(key,nonDefaultValues.get(key));
            }else {
                userData.put(key, defaultValues.get(key));
            }
        }
        return userData;
    }


}
