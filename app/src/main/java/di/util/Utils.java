package di.util;

import di.jsonparser.JsonParser;

import java.io.IOException;

public class Utils {
    public static String getResourceAsString(String resourceName) throws IOException {
        return new String(JsonParser.class.getResourceAsStream("/" + resourceName).readAllBytes());
    }
}
