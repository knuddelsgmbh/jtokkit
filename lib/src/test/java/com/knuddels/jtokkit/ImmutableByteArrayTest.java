package com.knuddels.jtokkit;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ImmutableByteArrayTest {

    @Test
    void canBeUsedAsKeyInMap() {
        var key1 = ImmutableByteArray.from("1, 2, 3");
        var key2 = ImmutableByteArray.from("1, 2, 3");

        assertEquals(key1, key2);
        assertEquals(key1.hashCode(), key2.hashCode());
    }

    @Test
    void canNotBeMutatedWhenUsingByteArrayConstructor() {
        var bytes = new byte[]{1, 2, 3};
        var byteArray = ImmutableByteArray.from(bytes);

        bytes[0] = 4;

        assertNotEquals(byteArray, ImmutableByteArray.from(bytes));
        assertEquals(byteArray, ImmutableByteArray.from(new byte[]{1, 2, 3}));
    }

    @Test
    void canNotBeMutatedWhenUsingGetRawArray() {
        var byteArray = ImmutableByteArray.from("1, 2, 3");
        var bytes = byteArray.getRawArray();

        bytes[0] = 4;

        assertNotEquals(byteArray, ImmutableByteArray.from(bytes));
        assertEquals(byteArray, ImmutableByteArray.from("1, 2, 3"));
    }

    @Test
    void getLengthIsCorrect() {
        var byteArray = ImmutableByteArray.from("1, 2, 3");

        assertEquals(7, byteArray.length());
    }

    @Test
    void getBytesBetweenReturnsCorrectSliceOfArray() {
        var byteArray = ImmutableByteArray.from(new byte[]{1, 2, 3, 4, 5, 6});

        assertEquals(ImmutableByteArray.from(new byte[]{4, 5, 6}), byteArray.getBytesBetween(3, 6));
    }

    @Test
    void getBytesBetweenThrowsWhenInclusiveStartIndexOutOfBounds() {
        var byteArray = ImmutableByteArray.from(new byte[]{1, 2, 3, 4, 5, 6});

        assertThrows(IndexOutOfBoundsException.class, () -> byteArray.getBytesBetween(-1, 6));
        assertThrows(IndexOutOfBoundsException.class, () -> byteArray.getBytesBetween(9, 10));
    }

    @Test
    void getBytesBetweenThrowsWhenExclusiveEndIndexOutOfBounds() {
        var byteArray = ImmutableByteArray.from(new byte[]{1, 2, 3, 4, 5, 6});

        assertThrows(IndexOutOfBoundsException.class, () -> byteArray.getBytesBetween(0, 7));
        assertThrows(IndexOutOfBoundsException.class, () -> byteArray.getBytesBetween(0, -1));
    }

    @Test
    void getBytesBetweenThrowsWhenStartIndexIsGreaterThanEndIndex() {
        var byteArray = ImmutableByteArray.from(new byte[]{1, 2, 3, 4, 5, 6});

        assertThrows(IllegalArgumentException.class, () -> byteArray.getBytesBetween(3, 2));
    }
}
