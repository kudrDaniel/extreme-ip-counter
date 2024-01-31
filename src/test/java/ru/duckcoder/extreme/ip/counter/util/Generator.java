package ru.duckcoder.extreme.ip.counter.util;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public final class Generator {


    private static void genIpFile(String path) throws IOException {
        try (var bos = new BufferedOutputStream(new FileOutputStream(path))) {
            for (int i = 0; i < 256; i++) {
                for (int j = 0; j < 256; j++) {
                    for (int k = 0; k < 16; k++) {
                        var line = ("0." + k + "." + j + "." + i + "\n").getBytes(StandardCharsets.UTF_8);
                        bos.write(line);
                    }
                }
            }
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
}
