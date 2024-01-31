package ru.duckcoder.extreme.ip.counter.io;

import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public final class IpFileReader implements Closeable {
    private int lastChar = 0;

    private final BufferedInputStream is;

    public IpFileReader(String path) throws FileNotFoundException {
        this.is = new BufferedInputStream(new FileInputStream(path));
    }

    public int readNextAddress() throws IOException {
        int i = 0, result = 0;
        int[] ints = new int[4];

        while ((lastChar = is.read()) != '\n' && lastChar != -1) {
            if (lastChar == '.') {
                i++;
            } else {
                ints[i] = ints[i] * 10 + lastChar - '0';
            }
        }

        i = 0;
        while (i < 4) {
            result = result << 8;
            result = result | ints[i];
            i++;
        }
        return result;
    }

    public boolean hasNextAddress() {
        return lastChar != -1;
    }

    @Override
    public void close() throws IOException {
        is.close();
    }
}
