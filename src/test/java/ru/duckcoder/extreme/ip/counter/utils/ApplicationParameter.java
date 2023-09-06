package ru.duckcoder.extreme.ip.counter.utils;

public enum ApplicationParameter {
    THREAD_0_IPS_256("256", "0"),
    THREAD_4_IPS_256("256", "4"),
    THREAD_8_IPS_256("256", "8"),
    THREAD_16_IPS_256("256", "16"),
    THREAD_32_IPS_256("256", "32"),
    THREAD_0_IPS_4096("4096", "0"),
    THREAD_4_IPS_4096("4096", "4"),
    THREAD_8_IPS_4096("4096", "8"),
    THREAD_16_IPS_4096("4096", "16"),
    THREAD_32_IPS_4096("4096", "32"),
    THREAD_0_IPS_65536("65536", "0"),
    THREAD_4_IPS_65536("65536", "4"),
    THREAD_8_IPS_65536("65536", "8"),
    THREAD_16_IPS_65536("65536", "16"),
    THREAD_32_IPS_65536("65536", "32"),
    THREAD_0_IPS_1048576("1048576", "0"),
    THREAD_4_IPS_1048576("1048576", "4"),
    THREAD_8_IPS_1048576("1048576", "8"),
    THREAD_16_IPS_1048576("1048576", "16"),
    THREAD_32_IPS_1048576("1048576", "32"),
    THREAD_0_IPS_16777216("16777216", "0"),
    THREAD_4_IPS_16777216("16777216", "4"),
    THREAD_8_IPS_16777216("16777216", "8"),
    THREAD_16_IPS_16777216("16777216", "16"),
    THREAD_32_IPS_16777216("16777216", "32");

    final String ipsCount, threadCount;

    ApplicationParameter(String ipsCount, String threadCount) {
        this.ipsCount = ipsCount;
        this.threadCount = threadCount;
    }

    public String getIpsCount() {
        return ipsCount;
    }

    public String getThreadCount() {
        return threadCount;
    }
}
