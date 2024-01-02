package com.knuddels.jtokkit;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ByteArrayWrapperTest {
    @Test
    public void getBytesBetweenReturnsCorrectSliceOfArray() {
        final ByteArrayWrapper byteArray = new ByteArrayWrapper(new byte[]{1, 2, 3, 4, 5, 6});

        assertEquals(new ByteArrayWrapper(new byte[]{4, 5, 6}), byteArray.getBytesBetween(3, 6));
    }

    @Test
    public void getBytesBetweenThrowsWhenInclusiveStartIndexOutOfBounds() {
        final ByteArrayWrapper byteArray = new ByteArrayWrapper(new byte[]{1, 2, 3, 4, 5, 6});

        assertThrows(IndexOutOfBoundsException.class, () -> byteArray.getBytesBetween(-1, 6));
        assertThrows(IndexOutOfBoundsException.class, () -> byteArray.getBytesBetween(9, 10));
    }

    @Test
    public void getBytesBetweenThrowsWhenExclusiveEndIndexOutOfBounds() {
        final ByteArrayWrapper byteArray = new ByteArrayWrapper(new byte[]{1, 2, 3, 4, 5, 6});

        assertThrows(IndexOutOfBoundsException.class, () -> byteArray.getBytesBetween(0, 7));
        assertThrows(IndexOutOfBoundsException.class, () -> byteArray.getBytesBetween(0, -1));
    }

    @Test
    public void getBytesBetweenThrowsWhenStartIndexIsGreaterThanEndIndex() {
        final ByteArrayWrapper byteArray = new ByteArrayWrapper(new byte[]{1, 2, 3, 4, 5, 6});

        assertThrows(IllegalArgumentException.class, () -> byteArray.getBytesBetween(3, 2));
    }
}
