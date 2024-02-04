package com.knuddels.jtokkit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ByteArrayWrapperTest {
    @Test
    void getBytesBetweenReturnsCorrectSliceOfArray() {
        ByteArrayWrapper byteArray = new ByteArrayWrapper(new byte[]{1, 2, 3, 4, 5, 6});

        assertEquals(new ByteArrayWrapper(new byte[]{4, 5, 6}), byteArray.getBytesBetween(3, 6));
    }

    @Test
    void getBytesBetweenThrowsWhenInclusiveStartIndexOutOfBounds() {
        ByteArrayWrapper byteArray = new ByteArrayWrapper(new byte[]{1, 2, 3, 4, 5, 6});

        assertThrows(IndexOutOfBoundsException.class, () -> byteArray.getBytesBetween(-1, 6));
        assertThrows(IndexOutOfBoundsException.class, () -> byteArray.getBytesBetween(9, 10));
    }

    @Test
    void getBytesBetweenThrowsWhenExclusiveEndIndexOutOfBounds() {
        ByteArrayWrapper byteArray = new ByteArrayWrapper(new byte[]{1, 2, 3, 4, 5, 6});

        assertThrows(IndexOutOfBoundsException.class, () -> byteArray.getBytesBetween(0, 7));
        assertThrows(IndexOutOfBoundsException.class, () -> byteArray.getBytesBetween(0, -1));
    }

    @Test
    void getBytesBetweenThrowsWhenStartIndexIsGreaterThanEndIndex() {
        ByteArrayWrapper byteArray = new ByteArrayWrapper(new byte[]{1, 2, 3, 4, 5, 6});

        assertThrows(IllegalArgumentException.class, () -> byteArray.getBytesBetween(3, 2));
    }
}
