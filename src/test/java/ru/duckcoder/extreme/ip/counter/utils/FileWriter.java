package ru.duckcoder.extreme.ip.counter.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileWriter {
    private final BufferedOutputStream bufferedOutputStream;

    public FileWriter(Path path) throws IOException {
        if (!Files.exists(path)) {
            Files.createFile(path);
        }
        this.bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(path.toString()));
    }

    public void write(byte[] bytes) throws IOException {
        this.bufferedOutputStream.write(bytes);
    }

    public void write(String string) throws IOException {
        this.write(string.getBytes(StandardCharsets.UTF_8));
    }

    public void close() throws IOException {
        this.bufferedOutputStream.close();
    }
}
