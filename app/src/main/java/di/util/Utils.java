package di.util;

import di.beanparser.JsonBeanParser;

import java.io.IOException;

public class Utils {
    public static String getResourceAsString(String resourceName) throws IOException {
        return new String(JsonBeanParser.class.getResourceAsStream("/" + resourceName).readAllBytes());
    }

    public static String capitalize(String str) {
        if (str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
