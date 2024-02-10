package com.knuddels.jtokkit.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IntArrayList {
    private int[] array;
    private int size = 0;

    /**
     * Constructs an empty list with an initial capacity of ten.
     */
    public IntArrayList() {
        this(10);
    }

    /**
     * Constructs an empty list with the specified initial capacity.
     *
     * @param initialCapacity the initial capacity of the list
     * @throws IllegalArgumentException if the specified initial capacity is negative
     */
    public IntArrayList(int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
        }

        array = new int[initialCapacity];
    }

    /**
     * Removes all the elements from this list. The list will
     * be empty after this call returns.
     */
    public void clear() {
        size = 0;
    }

    /**
     * Appends the specified element to the end of this list.
     *
     * @param element element to be appended to this list
     */
    public void add(int element) {
        if (size >= array.length) {
            resize();
        }
        array[size++] = element;
    }

    /**
     * Returns the element at the specified position in this list.
     *
     * @param index index of the element to return
     * @return the element at the specified position in this list
     * @throws IndexOutOfBoundsException if the index is out of range
     *         ({@code index &lt; 0 || index &gt;= size()})
     */
    public int get(int index) {
        return array[index];
    }

    /**
     * Replaces the element at the specified position in this list with
     * the specified element.
     *
     * @param index index of the element to replace
     * @param element element to be stored at the specified position
     * @return the element previously at the specified position
     * @throws IndexOutOfBoundsException if the index is out of range
     *         ({@code index &lt; 0 || index &gt;= size()})
     */
    public int set(int index, int element) {
        int old = array[index];
        array[index] = element;
        return old;
    }

    private void resize() {
        ensureCapacity(Math.max(1, array.length) * 2);
    }

    /**
     * Increases the capacity of this {@code IntArrayList} instance, if
     * necessary, to ensure that it can hold at least the number of elements
     * specified by the minimum capacity argument.
     *
     * @param minCapacity the desired minimum capacity
     */
    public void ensureCapacity(int minCapacity) {
        if (minCapacity <= size) {
            return;
        }
        int[] newArray = new int[minCapacity];
        if (size > 0) {
            System.arraycopy(array, 0, newArray, 0, size);
        }
        array = newArray;
    }

    /**
     * Returns the number of elements in this list.
     *
     * @return the number of elements in this list
     */
    public int size() {
        return size;
    }

    /**
     * Returns {@code true} if this list contains no elements.
     *
     * @return {@code true} if this list contains no elements
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns an array containing all the elements in this list
     * in proper sequence (from first to last element).
     *
     * <p>The returned array will be "safe" in that no references to it are
     * maintained by this list. (In other words, this method must allocate
     * a new array). The caller is thus free to modify the returned array.
     *
     * @return an array containing all the elements in this list in
     *         proper sequence
     */
    public int[] toArray() {
        return Arrays.copyOf(array, size);
    }

    /**
     * Returns a {@code List<Integer>} containing all the elements in this list
     * in proper sequence (from first to last element).
     *
     * <p>The returned list will be "safe" in that no reference to it are maintained
     * by this list. (In other words, this method must allocate a new list). The caller
     * is thus free to modify the returned list.
     *
     * @return a {@code List<Integer>} containing all the elements in this list in
     *         proper sequence
     */
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
