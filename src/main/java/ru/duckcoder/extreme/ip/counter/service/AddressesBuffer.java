package ru.duckcoder.extreme.ip.counter.service;

public class AddressesBuffer {
    private final boolean[][][][] bits;

    private final long maxSize;

    private long size;

    public AddressesBuffer() {
        bits = new boolean[256][][][];
        size = 0;
        maxSize = 256L * 256L * 256L * 256L;
    }

    public long getSize() {
        return this.size;
    }

    public synchronized boolean checkAddress(int[] sections) {
        if (sections == null) {
            return true;
        }
        if (size == maxSize) {
            return false;
        }
        if (this.bits[sections[0]] == null) {
            this.bits[sections[0]] = new boolean[256][][];
        }
        if (this.bits[sections[0]][sections[1]] == null) {
            this.bits[sections[0]][sections[1]] = new boolean[256][];
        }
        if (this.bits[sections[0]][sections[1]] == null) {
            this.bits[sections[0]][sections[1]] = new boolean[256][];
        }
        if (this.bits[sections[0]][sections[1]][sections[2]] == null) {
            this.bits[sections[0]][sections[1]][sections[2]] = new boolean[256];
        }
        if (this.bits[sections[0]][sections[1]][sections[2]][sections[3]]) {
            return true;
        }
        this.bits[sections[0]][sections[1]][sections[2]][sections[3]] = true;
        size++;
        return true;
    }
}
