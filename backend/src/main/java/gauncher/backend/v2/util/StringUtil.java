package gauncher.backend.v2.util;

public class StringUtil {

    public static String shortify(String string) {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == ' ') {
                res.append(c);
            }
        }
        return res.toString();
    }
}
