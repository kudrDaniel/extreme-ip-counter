package ru.duckcoder.extreme.ip.counter.utils;

public class Timer {
    public static String timeToPattern(long time) {
        int[] sections = {0, 0, 0, 0};
        sections[3] = (int) (time % 1000);
        if (sections[3] < 100) {
            sections[3] *= 10;
        }
        time = time / 1000;
        sections[2] = (int) (time % 60);
        time = time / 60;
        sections[1] = (int) (time % 60);
        sections[0] = (int) (time / 60);
        return String.format("%d h, %d m, %d s, %d ms",
                sections[0],
                sections[1],
                sections[2],
                sections[3]);
    }
}
