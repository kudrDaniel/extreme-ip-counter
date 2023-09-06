package ru.duckcoder.extreme.ip.counter;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import ru.duckcoder.extreme.ip.counter.utils.ApplicationParameter;
import ru.duckcoder.extreme.ip.counter.utils.FileWriter;
import ru.duckcoder.extreme.ip.counter.utils.Timer;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ApplicationTest {
    private static final int ATTEMPT_COUNT = 5;

    private static final String RESULT_NAME = "result.txt";

    private static final String IPS_NAME = "ips.txt";

    private static final Path resultPath = Paths.get("src", "test", "resources", RESULT_NAME).toAbsolutePath();

    private static final Path ipsPath = Paths.get("src", "test", "resources", IPS_NAME).toAbsolutePath();

    private static final FileWriter fileWriter;

    static {
        try {
            fileWriter = new FileWriter(ApplicationTest.resultPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeAll
    public static void beforeTests() throws IOException {
        ManagementFactory.getRuntimeMXBean().getInputArguments().forEach(System.out::println);
        StringBuilder stringBuilder = new StringBuilder(
                "IPs Count\t"
                + "Thread Count\t"
        );
        for (int attempt = 0; attempt < ATTEMPT_COUNT; attempt++) {
            stringBuilder.append("Attempt #");
            stringBuilder.append(attempt);
            stringBuilder.append('\t');
        }
        stringBuilder.append("Average Time\n");
        fileWriter.write(stringBuilder.toString());
    }

    @ParameterizedTest
    @EnumSource(ApplicationParameter.class)
    public void applicationTest(ApplicationParameter parameter) throws IOException {
        StringBuilder stringBuilder = new StringBuilder(
                parameter.getIpsCount() + '\t'
                + parameter.getThreadCount() + '\t'
        );
        long[] elapsedTimes = new long[ATTEMPT_COUNT];
        long averageTime = 0;
        for (int attempt = 0; attempt < ATTEMPT_COUNT; attempt++) {
            Application.main(new String[]{ipsPath.toString(), "/GR", parameter.getIpsCount()});
            elapsedTimes[attempt] = System.currentTimeMillis();
            Application.main(new String[]{ipsPath.toString(), parameter.getThreadCount()});
            elapsedTimes[attempt] = System.currentTimeMillis() - elapsedTimes[attempt];
            averageTime += elapsedTimes[attempt];
            stringBuilder.append(Timer.timeToPattern(elapsedTimes[attempt]));
            stringBuilder.append('\t');
        }
        stringBuilder.append(Timer.timeToPattern(averageTime / ATTEMPT_COUNT));
        stringBuilder.append('\n');
        fileWriter.write(stringBuilder.toString());
    }

    @AfterAll
    public static void afterTests() throws IOException {
        ApplicationTest.fileWriter.close();
    }
}
