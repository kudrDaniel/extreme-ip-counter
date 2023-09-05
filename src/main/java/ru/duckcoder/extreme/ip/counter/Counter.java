package ru.duckcoder.extreme.ip.counter;

import ru.duckcoder.extreme.ip.counter.reader.ChannelProvider;
import ru.duckcoder.extreme.ip.counter.reader.FileProvider;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Counter {
    private static final char EOF = (char) -1;

    private static final ChannelProvider channelProvider = new ChannelProvider();

    public static long count(Path filePath) throws IOException {
        long fileSize = Files.size(filePath);
        FileProvider fileProvider = new FileProvider(new FileInputStream(filePath.toString()), 0, fileSize);
        long uniqueCount = 0;
        while (fileProvider.getBytesReadRemain() != 0) {
            String line = fileProvider.read();
            if (line.length() < 7) {
                break;
            }
            int[] sections = new int[4];
            if (line.matches("^((25[0-5]|(2[0-4]|1\\d|[1-9]|)\\d)\\.?\\b){4}$")) {
                String[] stringSections = line.split("\\.");
                for (int i = 0; i < 4; i++) {
                    sections[i] = Integer.parseInt(stringSections[i]);
                }
            } else {
                continue;
            }
            uniqueCount += Counter.channelProvider.setAddress(getBytePos(sections), getBitPos(sections));
        }
        channelProvider.closeChannelAndDeleteTempFile();
        return uniqueCount;
    }

    private static long getBytePos(int[] sections) {
        return ((long) sections[0] * 256 * 256 * 256
                + (long) sections[1] * 256 * 256
                + (long) sections[2] * 256
                + (long) sections[3]) / 8;
    }

    private static int getBitPos(int[] sections) {
        return (int) (((long) sections[0] * 256 * 256 * 256
                + (long) sections[1] * 256 * 256
                + (long) sections[2] * 256
                + (long) sections[3]) % 8);
    }
}
