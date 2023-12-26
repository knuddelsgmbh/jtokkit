package com.knuddels.jtokkit;

import java.util.Arrays;

public class ByteArrayList {
    private byte[] array;
    private int size;

    public ByteArrayList() {
        array = new byte[10];
        size = 0;
    }

    public void clear() {
        size = 0;
    }

    public void add(byte element) {
        if (size >= array.length) {
            resize();
        }
        array[size++] = element;
    }

    private void resize() {
        byte[] newArray = new byte[array.length * 2];
        System.arraycopy(array, 0, newArray, 0, size);
        array = newArray;
    }

    int length() {
        return size;
    }

    public byte[] toByteArray() {
        return Arrays.copyOf(array, size);
    }
}
