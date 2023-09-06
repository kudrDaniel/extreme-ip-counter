package ru.duckcoder.extreme.ip.counter.io;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileProvider {
    private BufferedInputStream bufferedInputStream;

    private BufferedOutputStream bufferedOutputStream;

    private long bytesReadRemain;

    public FileProvider(FileInputStream fileInputStream, long offset, long length) throws IOException {
        this.bytesReadRemain = length;
        fileInputStream.skip(offset);
        bufferedInputStream = new BufferedInputStream(fileInputStream);
    }

    public FileProvider(FileOutputStream fileOutputStream) {
        this.bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
    }

    public synchronized String read() throws IOException {
        int c;
        StringBuilder stringBuilder = new StringBuilder();
        while ((c = this.bufferedInputStream.read()) != '\n' && c != -1) {
            stringBuilder.append((char) c);
            bytesReadRemain--;
        }
        if (c == '\n') {
            bytesReadRemain--;
        }
        return stringBuilder.toString();
    }

    public long getBytesReadRemain() {
        return this.bytesReadRemain;
    }

    public void write(String line) {
        try {
            bufferedOutputStream.write((line).getBytes(StandardCharsets.UTF_8));
        } catch (IOException writeLineException) {
            System.out.println(writeLineException.getMessage());
        }
    }

    public void close() throws IOException {
        if (this.bufferedInputStream != null) {
            this.bufferedInputStream.close();
        }
        if (this.bufferedOutputStream != null) {
            this.bufferedOutputStream.close();
        }
    }
}
