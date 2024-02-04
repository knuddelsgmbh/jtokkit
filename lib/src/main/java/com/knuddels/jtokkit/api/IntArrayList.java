package com.knuddels.jtokkit.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IntArrayList {
    private int[] array;
    private int size = 0;

    public IntArrayList() {
        this(10);
    }

    public IntArrayList(int size) {
        array = new int[size];
    }

    public void clear() {
        size = 0;
    }

    public void add(int element) {
        if (size >= array.length) {
            resize();
        }
        array[size++] = element;
    }

    public int get(int index) {
        return array[index];
    }

    public int set(int index, int element) {
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
        int[] newArray = new int[targetSize];
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

    public int[] toArray() {
        return Arrays.copyOf(array, size);
    }

    public List<Integer> boxed() {
        List<Integer> list = new ArrayList<>(size);
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
        IntArrayList that = (IntArrayList) o;
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
