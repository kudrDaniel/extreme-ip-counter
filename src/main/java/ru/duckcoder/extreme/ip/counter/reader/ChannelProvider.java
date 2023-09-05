package ru.duckcoder.extreme.ip.counter.reader;

import ru.duckcoder.extreme.ip.counter.CounterController;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class ChannelProvider {
    private FileChannel channel;
    private final byte[] bytes = {
            0b00000001,
            0b00000010,
            0b00000100,
            0b00001000,
            0b00010000,
            0b00100000,
            0b01000000,
            ~0b01111111
    };

    private Path filePath = null;

    public ChannelProvider() {
        try {
            this.filePath = Files.createTempFile("counterHelper", ".bin");
        } catch (IOException tempFileCreationException) {
            System.out.println(tempFileCreationException.getMessage());
        }
        try {
            this.channel = FileChannel.open(this.filePath, StandardOpenOption.READ, StandardOpenOption.WRITE);
            this.channel.force(false);
        } catch (IOException channelCreationException) {
            System.out.println(channelCreationException.getMessage());
        }
    }

    public int setAddress(long bytePos, int bitPos) throws IOException {
        int writeBytes = 0;
        ByteBuffer buffer = ByteBuffer.allocate(1);
        int readBytes = this.channel.read(buffer, bytePos);
        if (readBytes == -1) {
            buffer.put(bytes[bitPos]);
            buffer.flip();
            writeBytes = this.channel.write(buffer, bytePos);
        } else if (readBytes == 0) {
            throw new RuntimeException("None bytes read!");
        } else {
            buffer.flip();
            byte check = (byte) (buffer.get(0) & bytes[bitPos]);
            if (check == 0) {
                byte value = (byte) (buffer.get() | bytes[bitPos]);
                buffer.clear();
                buffer.put(value);
                buffer.flip();
                writeBytes = this.channel.write(buffer, bytePos);
            }
        }
        return writeBytes;
    }

    public void closeChannelAndDeleteTempFile() {
        try {
            this.channel.close();
            Files.delete(this.filePath);
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }
}
