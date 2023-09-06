package ru.duckcoder.extreme.ip.counter;

import ru.duckcoder.extreme.ip.counter.utils.Generator;
import ru.duckcoder.extreme.ip.counter.utils.Timer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Application {
    public static void main(String[] args) {
        if (args.length < 2) {
            return;
        }
        Path ipsFile = Paths.get(args[0]);
        if (!Files.exists(ipsFile.toAbsolutePath())) {
            try {
                Files.createFile(ipsFile.toAbsolutePath());
            } catch (IOException createNewIpsFileException) {
                System.out.println(createNewIpsFileException.getMessage());
            }
        }
        int threadCount = 8;
        if (args.length == 3 && args[1].equals("/GP")) {
            try {
                long counter = Generator.generateIpsByPattern(ipsFile, Integer.parseInt(args[1]));
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
            }
        } else if (args.length == 3 && args[1].equals("/GR")) {
            try {
                long counter = Generator.generateRandomIps(ipsFile, Long.parseLong(args[2]));
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
            }
        } else if (args.length == 2 && (threadCount = Integer.parseInt(args[1])) >= 0 && threadCount <= 32) {
            try {
                CounterController counterController = new CounterController(ipsFile);
                if (threadCount == 0) {
                    counterController.count();
                } else {
                    counterController.concurrentCount(threadCount);
                }
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
            }
        }
    }
}
