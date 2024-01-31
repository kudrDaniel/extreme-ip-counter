package ru.duckcoder.extreme.ip.counter;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.instrument.Instrumentation;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Random;

public final class Application {
    public static void main(String[] args) {
        Runtime.getRuntime().maxMemory();
        var path = getPathFromArgs(args);
        try {
            switch (path) {
                case "ips_generated.txt" -> genIpFile(path);
                case "ips_random.txt" -> genRandomIpFile(path, 1048576);
                default -> {
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        var beginCountingMillis = System.currentTimeMillis();
        System.out.println("Unique ip count while limitless counting: " + Counter.count(path));
        var time = new Calendar.Builder().setInstant(System.currentTimeMillis() - beginCountingMillis).build();
        var timeString = (time.get(Calendar.HOUR) - 3) + "h " + time.get(Calendar.MINUTE) + "m " + time.get(Calendar.SECOND) + "s " + time.get(Calendar.MILLISECOND) + "ms";
        System.out.println("Time elapsed: " + timeString);
    }

    private static void genIpFile(String path) throws IOException {
        try (var bos = new BufferedOutputStream(new FileOutputStream(path))) {
//            for (int i = 0; i < 256 * 256 * 16; i++) {
//                var line = ("1.1.1.1\n").getBytes(StandardCharsets.UTF_8);
//                bos.write(line);
//            }
            for (int i = 0; i < 256; i++) {
                for (int j = 0; j < 256; j++) {
                    for (int k = 0; k < 16; k++) {
                        var line = ("0." + k + "." + j + "." + i + "\n").getBytes(StandardCharsets.UTF_8);
                        bos.write(line);
                    }
                }
            }
//            for (int i = 0; i < 256; i++) {
//                for (int j = 0; j < 256; j++) {
//                    var line = (j + "." + i + "." + j + "." + i + "\n").getBytes(StandardCharsets.UTF_8);
//                    bos.write(line);
//                }
//            }
        }
    }

    private static void genRandomIpFile(String path, int count) throws IOException {
        try (var bos = new BufferedOutputStream(new FileOutputStream(path))) {
            var rnd = new Random();
            for (int i = 0; i < count; i++) {
                var line = (rnd.nextInt(0, 256) + "."
                        + rnd.nextInt(0, 256) + "."
                        + rnd.nextInt(0, 256) + "."
                        + rnd.nextInt(0, 256) + "\n").getBytes(StandardCharsets.UTF_8);
                var duplicateCount = rnd.nextInt(1, 4);
                for (int j = 0; j < duplicateCount; j++) {
                    bos.write(line);
                }
                i += duplicateCount - 1;
            }
        }
    }

    private static String getPathFromArgs(String[] args) {
        if (args == null) {
            throw new RuntimeException("File path must be declared");
        }
        return args[0];
    }
}
