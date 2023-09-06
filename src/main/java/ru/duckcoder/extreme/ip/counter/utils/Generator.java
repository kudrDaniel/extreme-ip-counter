package ru.duckcoder.extreme.ip.counter.utils;

import ru.duckcoder.extreme.ip.counter.io.FileProvider;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Random;

public class Generator {
    public static long generateRandomIps(Path filePath, long count) throws IOException {
        FileProvider fileProvider = new FileProvider(new FileOutputStream(filePath.toString()));
        Random rnd = new Random(System.currentTimeMillis());
        for (long i = 0; i < count; i++) {
            fileProvider.write(String.format("%d.%d.%d.%d%s",
                    rnd.nextInt(0,256),
                    rnd.nextInt(0,256),
                    rnd.nextInt(0,256),
                    rnd.nextInt(0,256),
                    i == count - 1 ? "" : "\n"
            ));
        }
        fileProvider.close();
        return count;
    }

    public static long generateIpsByPattern(Path filePath, int step) throws IOException {
        FileProvider fileProvider = new FileProvider(new FileOutputStream(filePath.toString(), true));
        Random rnd = new Random(System.currentTimeMillis());
        long counter = 0;
        int initial = step - 1;
        int max = 255;
        for (int sec0 = initial; sec0 <= max; sec0 += step) {
            for (int sec1 = initial; sec1 <= max; sec1 += step) {
                for (int sec2 = initial; sec2 <= max; sec2 += step) {
                    for (int sec3 = initial; sec3 <= max; sec3 += step) {
                        fileProvider.write(String.format("%d.%d.%d.%d%s",
                                sec0,
                                sec1,
                                sec2,
                                sec3,
                                sec0 == max && sec1 == max && sec2 == max && sec3 == max ? "" : "\n"
                        ));
                        counter++;
                    }
                }
            }
        }
        fileProvider.close();
        return counter;
    }
}
