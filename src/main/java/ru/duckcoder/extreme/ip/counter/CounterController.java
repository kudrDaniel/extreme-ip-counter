package ru.duckcoder.extreme.ip.counter;

import ru.duckcoder.extreme.ip.counter.concurrent.Counter;
import ru.duckcoder.extreme.ip.counter.reader.ChannelProvider;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class CounterController {
    private static final AtomicLong uniqueCount = new AtomicLong(0);

    private static final ChannelProvider channelProvider = new ChannelProvider();

    public static final boolean[][][][] uniqueAddresses = new boolean[256][256][256][256];

    private static final List<Long> lockBytes = new ArrayList<>();

    private static long offset, length;

    public static long concurrentCount(Path path, int threadCount) throws IOException, InterruptedException {
        offset = 0;
        length = Files.size(path) / threadCount;
        length = getNextLength(path);
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < threadCount; i++) {
            Counter counter = new Counter(path, offset, length, channelProvider);
            offset = getNextOffset();
            if (i == threadCount - 1) {
                length = Files.size(path) - offset;
            } else {
                length = getNextLength(path);
            }
            Thread thread = new Thread(counter, "Counter #" + i);
            thread.start();
            threads.add(thread);
        }
        for (var thread: threads) {
            thread.join();
        }
        channelProvider.closeChannelAndDeleteTempFile();
        return uniqueCount.get();
    }

    public synchronized static void applyCounting(long uniqueCount) {
        CounterController.uniqueCount.addAndGet(uniqueCount);
    }

    public static boolean isChannelLockedBytes(long bytePos) {
        return lockBytes.contains(bytePos);
    }

    public static void addLockByte(long bytePos) {
        lockBytes.add(bytePos);
    }

    public static void removeLockByte(long bytePos) {
        lockBytes.remove(bytePos);
    }

    private static long getNextOffset(){
        return offset + length;
    }

    private static long getNextLength(Path path) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(path.toString());
        fileInputStream.skip(offset + length);
        long nextCounter = 0;
        int c;
        while ((c = fileInputStream.read()) != '\n' && c != -1) {
            nextCounter++;
        }
        if (c == '\n') {
            nextCounter++;
        }
        return length + nextCounter;
    }
}
