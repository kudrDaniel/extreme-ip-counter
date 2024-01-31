package ru.duckcoder.extreme.ip.counter;
import ru.duckcoder.extreme.ip.counter.io.IpFileReader;

import java.io.FileNotFoundException;
import java.io.IOException;

public final class Counter {
    private static final byte[] BIT_MASKS = {
            0b00000001,
            0b00000010,
            0b00000100,
            0b00001000,
            0b00010000,
            0b00100000,
            0b01000000,
            ~0b01111111
    };

    public static long count(String path) {
        long uniqueCount = 0L;
        try (var fr = new IpFileReader(path)) {
            byte[] buffer = new byte[1 << 29];
            while (fr.hasNextAddress()) {
                int address = fr.readNextAddress();
                int byteIndex = address >>> 3;
                int bitIndex = address & 0x7;

                byte readByte = buffer[byteIndex];
                if (readByte == ~0b00000000) {
                    continue;
                }
                var isChecked = false;
                if (readByte != 0) {
                    isChecked = ((readByte & BIT_MASKS[bitIndex]) != 0);
                }
                if (!isChecked) {
                    readByte = (byte) (readByte | BIT_MASKS[bitIndex]);
                    buffer[byteIndex] = readByte;
                    uniqueCount++;
                }
                if (uniqueCount == (1L << 32)) {
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found", e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return uniqueCount;
    }
}
