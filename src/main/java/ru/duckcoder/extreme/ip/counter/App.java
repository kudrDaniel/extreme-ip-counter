package ru.duckcoder.extreme.ip.counter;

import ru.duckcoder.extreme.ip.counter.util.Timer;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class App {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        Path ipsFile = Paths.get("ips.txt");
        if (!Files.exists(ipsFile.toAbsolutePath())) {
            try {
                Files.createFile(ipsFile.toAbsolutePath());
            } catch (IOException createNewIpsFileException) {
                System.out.println(createNewIpsFileException.getMessage());
            }
        }
        int threadCount = 8;
        if (args.length == 2 && args[0].equals("/GP")) {
            try {
                long counter = Generator.generateIpsByPattern(ipsFile, Integer.parseInt(args[1]));
                System.out.printf("Generated %d ips%n", counter);
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
            }
        } else if (args.length == 2 && args[0].equals("/GR")) {
            try {
                long counter = Generator.generateRandomIps(ipsFile, Long.parseLong(args[1]));
                System.out.printf("Generated %d ips%n", counter);
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
            }
        } else if (args.length == 1 && (threadCount = Integer.parseInt(args[0])) > 0 && threadCount <= 32) {
            try {
                long testStart = System.currentTimeMillis();
                System.out.println(CounterController.concurrentCount(ipsFile, threadCount));
                long testElapsed = System.currentTimeMillis() - testStart;
                System.out.println("Multi thread time: " + Timer.timeToPattern(testElapsed));

                testStart = System.currentTimeMillis();
                System.out.println(Counter.count(ipsFile));
                testElapsed = System.currentTimeMillis() - testStart;
                System.out.println("One thread time: " + Timer.timeToPattern(testElapsed));
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
            }
        }
        long elapsedTime = System.currentTimeMillis() - startTime;
        System.out.println(Timer.timeToPattern(elapsedTime));
    }
}
