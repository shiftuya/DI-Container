package di.util;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Utils {
    public static String getResourceAsString(String resourceName) throws IOException {
        return new String(Utils.class.getResourceAsStream("/" + resourceName).readAllBytes());
    }

    public static String capitalize(String str) {
        if (str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static List<String> splitByComma(String str) {
        return Arrays.asList(str.split(","));
    }
}
