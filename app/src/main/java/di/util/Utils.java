package di.util;

import di.beanparser.JsonBeanParser;

import java.io.IOException;

public class Utils {
    public static String getResourceAsString(String resourceName) throws IOException {
        return new String(JsonBeanParser.class.getResourceAsStream("/" + resourceName).readAllBytes());
    }
}
