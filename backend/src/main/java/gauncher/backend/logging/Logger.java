package gauncher.backend.logging;

import gauncher.backend.util.StringUtil;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class Logger {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";

    private static StringUtil stringUtil = new StringUtil();
    private static int count = 1;
    private final String name;
    private final SimpleDateFormat simpleDateFormat;

    public Logger(String name) {
        this.name = name;
        this.simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    public Logger() {
        this(String.format("logger-%s", count++));
    }

    public void info(String message) {
        System.out.printf(
                "%s[%s] INFO - %s - %s%s\n",
                ANSI_GREEN, simpleDateFormat.format(new Date()), this.name, ANSI_RESET, message);
    }

    public void info(String message, Object... args) {
        var msg = stringUtil.format(message, args);
        System.out.printf(
                "%s[%s] INFO - %s - %s%s\n",
                ANSI_GREEN, simpleDateFormat.format(new Date()), this.name, ANSI_RESET, msg);
    }

    public void error(String message, Object... args) {
        var msg = stringUtil.format(message, args);
        System.err.printf(
                "%s[%s] ERROR - %s - %s%s\n",
                ANSI_RED, simpleDateFormat.format(new Date()), this.name, ANSI_RESET, msg);
    }

    public void debug(String message) {
        System.out.printf(
                "%s[%s] DEBUG - %s - %s%s\n",
                ANSI_YELLOW, simpleDateFormat.format(new Date()), this.name, ANSI_RESET, message);
    }

    public void debug(String message, Object... args) {
        var msg = stringUtil.format(message, args);
        System.out.printf(
                "%s[%s] DEBUG - %s - %s%s\n",
                ANSI_YELLOW, simpleDateFormat.format(new Date()), this.name, ANSI_RESET, msg);
    }

    public void error(String message) {
        System.err.printf(
                "%s[%s] ERROR - %s - %s%s\n",
                ANSI_RED, simpleDateFormat.format(new Date()), this.name, ANSI_RESET, message);
    }
}
