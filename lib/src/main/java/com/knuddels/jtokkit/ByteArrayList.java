package com.knuddels.jtokkit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ByteArrayList {
    private byte[] array;
    private int size = 0;

    public ByteArrayList() {
        this(10);
    }

    public ByteArrayList(int size) {
        array = new byte[size];
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

    public byte get(int index) {
        return array[index];
    }

    public int set(int index, byte element) {
        int old = array[index];
        array[index] = element;
        return old;
    }

    private void resize() {
        ensureCapacity(Math.max(1, array.length) * 2);
    }

    public void ensureCapacity(int targetSize) {
        if (targetSize <= size) {
            return;
        }
        byte[] newArray = new byte[targetSize];
        if (size > 0) {
            System.arraycopy(array, 0, newArray, 0, size);
        }
        array = newArray;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public byte[] toArray() {
        return Arrays.copyOf(array, size);
    }

    public List<Byte> boxed() {
        List<Byte> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            list.add(array[i]);
        }
        return list;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ByteArrayList that = (ByteArrayList) o;
        if (size != that.size) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (array[i] != that.array[i]) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 1;
        for (int i = 0; i < size; i++) {
            result = 31 * result + array[i];
        }
        return result;
    }

    @Override
    public String toString() {
        return boxed().toString();
    }
}
