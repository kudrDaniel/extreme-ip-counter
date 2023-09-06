package ru.duckcoder.extreme.ip.counter.service;

import ru.duckcoder.extreme.ip.counter.CounterController;
import ru.duckcoder.extreme.ip.counter.io.FileProvider;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;

public class Counter implements Runnable {
    private final Path path;

    private final long offset, length;

    private final AddressesBuffer addressesBuffer;

    public Counter(Path path, long offset, long length, AddressesBuffer addressesBuffer) {
        this.path = path;
        this.offset = offset;
        this.length = length;
        this.addressesBuffer = addressesBuffer;
    }

    @Override
    public void run() {
        try {
            this.count();
        } catch (IOException ioException) {
            System.out.println(ioException.getMessage());
        }
    }

    private void count() throws IOException {
        FileInputStream fileInputStream = new FileInputStream(this.path.toString());
        FileProvider fileProvider = new FileProvider(fileInputStream, this.offset, this.length);
        while (fileProvider.getBytesReadRemain() != 0) {
            String line = fileProvider.read();
            if (line.length() < 7) {
                break;
            }
            int[] sections = getAddressSections(line);
            if (!this.addressesBuffer.checkAddress(sections)) {
                return;
            }
        }
        fileProvider.close();
    }

    private int[] getAddressSections(String address) {
        int[] sections = new int[4];
        if (address.matches("^((25[0-5]|(2[0-4]|1\\d|[1-9]|)\\d)\\.?\\b){4}$")) {
            String[] stringSections = address.split("\\.");
            for (int i = 0; i < 4; i++) {
                sections[i] = Integer.parseInt(stringSections[i]);
            }
        } else {
            return null;
        }
        return sections;
    }
}
