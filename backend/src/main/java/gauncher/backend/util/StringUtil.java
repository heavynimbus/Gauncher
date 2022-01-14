package gauncher.backend.util;

public class StringUtil {

    public String format(String message, Object[] args) {
        for (Object o : args) {
            message = message.replaceFirst("%s", o.toString());
        }
        return message;
    }
}
