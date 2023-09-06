package ru.duckcoder.extreme.ip.counter;

import ru.duckcoder.extreme.ip.counter.service.Counter;
import ru.duckcoder.extreme.ip.counter.service.AddressesBuffer;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

public class CounterController {
    private final AddressesBuffer addressesBuffer;

    private final List<Integer[]> lockedBits = new ArrayList<>();

    private List<Future<Long>> futures;

    private final Path path;

    private long offset, length, fileSize;

    public CounterController(Path path) throws IOException {
        this.addressesBuffer = new AddressesBuffer();
        this.path = path;
        this.offset = 0;
        this.length = Files.size(this.path);
        this.fileSize = length;
    }

    public long count() {
        //Standard counting
        Counter counter = new Counter(this.path, this.offset, this.length, this.addressesBuffer);
        counter.run();
        return this.addressesBuffer.getSize();
    }

    public long concurrentCount(int threadCount) throws IOException, InterruptedException {
        //Concurrent counting
        this.offset = 0;
        this.length /= threadCount;
        this.length = getNextLength();
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < threadCount; i++) {
            Counter counter = new Counter(this.path, this.offset, this.length, this.addressesBuffer);
            this.offset = getNextOffset();
            if (i == threadCount - 1) {
                this.length = this.fileSize - this.offset;
            } else {
                this.length = getNextLength();
            }
            Thread thread = new Thread(counter, "Counter #" + i);
            thread.start();
            threads.add(thread);
        }
        for (var thread: threads) {
            thread.join();
        }
        return this.addressesBuffer.getSize();
    }

    public AddressesBuffer getAddressesBuffer() {
        return this.addressesBuffer;
    }

    private long getNextOffset(){
        return this.offset + this.length;
    }

    private long getNextLength() throws IOException {
        FileInputStream fileInputStream = new FileInputStream(this.path.toString());
        fileInputStream.skip(this.offset + this.length);
        long nextCounter = 0;
        int c;
        while ((c = fileInputStream.read()) != '\n' && c != -1) {
            nextCounter++;
        }
        if (c == '\n') {
            nextCounter++;
        }
        return this.length + nextCounter;
    }
}
