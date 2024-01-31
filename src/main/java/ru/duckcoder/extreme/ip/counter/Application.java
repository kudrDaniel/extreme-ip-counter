package ru.duckcoder.extreme.ip.counter;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Random;

public final class Application {
    public static void main(String[] args) {
        var path = getPathFromArgs(args);

        var beginCountingMillis = System.currentTimeMillis();
        System.out.println("Unique ip count while limitless counting: " + Counter.count(path));
        var time = new Calendar.Builder().setInstant(System.currentTimeMillis() - beginCountingMillis).build();
        var timeString = (time.get(Calendar.HOUR) - 3) + "h " + time.get(Calendar.MINUTE) + "m " + time.get(Calendar.SECOND) + "s " + time.get(Calendar.MILLISECOND) + "ms";
        System.out.println("Time elapsed: " + timeString);
    }

    private static String getPathFromArgs(String[] args) {
        if (args == null) {
            throw new RuntimeException("File path must be declared");
        }
        return args[0];
    }
}
