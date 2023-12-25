package com.knuddels.jtokkit;

import java.util.Arrays;

class ByteArrayWrapper {
    private final byte[] array;

    /*
     * Creates a new instance of ByteArrayWrapper from the given array.
     * The given array is not copied, so every calling method in this class must make sure
     * to never pass an array which reference leaked to the outside. Since some of our
     * construction methods already create new arrays, we do not want to copy here in this
     * constructor again.
     */
    ByteArrayWrapper(byte[] array) {
        this.array = array;
    }

    /**
     * Returns the length of this array.
     *
     * @return the length of this array.
     */
    public int length() {
        return array.length;
    }

    /**
     * Returns the bytes of this array from startIndex (inclusive) to endIndex (exclusive). The returned array is a copy
     * of the original array.
     *
     * @param startIndex the index from which to start copying (inclusive)
     * @param endIndex   the index at which to stop copying (exclusive)
     * @return a new {@link ByteArrayWrapper} containing the bytes from startIndex (inclusive) to endIndex (exclusive)
     * @throws IllegalArgumentException if startIndex is out of bounds, endIndex is out of bounds or endIndex is less than
     *                                  startIndex
     */
    public ByteArrayWrapper getBytesBetween(int startIndex, int endIndex) {
        if (startIndex < 0 || startIndex >= array.length) {
            throw new IndexOutOfBoundsException("startIndex out of bounds: " + startIndex + " (" + this + ")");
        } else if (endIndex < 0 || endIndex > array.length) {
            throw new IndexOutOfBoundsException("endIndex out of bounds: " + endIndex + " (" + this + ")");
        } else if (startIndex >= endIndex) {
            throw new IllegalArgumentException("startIndex must be less than endIndex: " + startIndex + " >= " + endIndex);
        }

        int length = endIndex - startIndex;
        byte[] result = new byte[length];
        System.arraycopy(array, startIndex, result, 0, length);
        return new ByteArrayWrapper(result);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        } else if (other == null || getClass() != other.getClass()) {
            return false;
        }

        ByteArrayWrapper that = (ByteArrayWrapper) other;
        return Arrays.equals(array, that.array);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(array);
    }

    @Override
    public String toString() {
        return Arrays.toString(array);
    }
}
