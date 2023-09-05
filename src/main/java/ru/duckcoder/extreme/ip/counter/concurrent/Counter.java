package ru.duckcoder.extreme.ip.counter.concurrent;

import ru.duckcoder.extreme.ip.counter.CounterController;
import ru.duckcoder.extreme.ip.counter.reader.ChannelProvider;
import ru.duckcoder.extreme.ip.counter.reader.FileProvider;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Counter implements Runnable {
    private final Path path;

    private final long offset, length;

    private final ChannelProvider channelProvider;

    public Counter(Path path, long offset, long length, ChannelProvider channelProvider) {
        this.path = path;
        this.offset = offset;
        this.length = length;
        this.channelProvider = channelProvider;
    }

    private long count() throws IOException {
        FileInputStream fileInputStream = new FileInputStream(this.path.toString());
        FileProvider fileProvider = new FileProvider(fileInputStream, this.offset, this.length);
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
            long bytePos = getBytePos(sections);
//            while (!CounterController.isChannelLockedBytes(bytePos)) {
//                CounterController.addLockByte(bytePos);
//                uniqueCount += this.channelProvider.setAddress(bytePos, getBitPos(sections));
//            }
//            CounterController.removeLockByte(bytePos);
            if (!CounterController.uniqueAddresses[sections[0]][sections[1]][sections[2]][sections[3]]) {
                CounterController.uniqueAddresses[sections[0]][sections[1]][sections[2]][sections[3]] = true;
                uniqueCount++;
            }
        }
        return uniqueCount;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " has been started");
        long uniqueCount = 0;
        try {
            uniqueCount = this.count();
        } catch (IOException ioException) {
            System.out.println(ioException.getMessage());
            ioException.printStackTrace();
        }
        CounterController.applyCounting(uniqueCount);
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
