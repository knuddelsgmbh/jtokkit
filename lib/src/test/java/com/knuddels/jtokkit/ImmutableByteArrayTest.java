package com.knuddels.jtokkit;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ImmutableByteArrayTest {

	@Test
	public void canBeUsedAsKeyInMap() {
		final ImmutableByteArray key1 = ImmutableByteArray.from("1, 2, 3");
		final ImmutableByteArray key2 = ImmutableByteArray.from("1, 2, 3");

		assertEquals(key1, key2);
		assertEquals(key1.hashCode(), key2.hashCode());
	}

	@Test
	public void canNotBeMutatedWhenUsingByteArrayConstructor() {
		final byte[] bytes = new byte[]{1, 2, 3};
		final ImmutableByteArray byteArray = ImmutableByteArray.from(bytes);

		bytes[0] = 4;

		assertNotEquals(byteArray, ImmutableByteArray.from(bytes));
		assertEquals(byteArray, ImmutableByteArray.from(new byte[]{1, 2, 3}));
	}

	@Test
	public void canNotBeMutatedWhenUsingGetRawArray() {
		final ImmutableByteArray byteArray = ImmutableByteArray.from("1, 2, 3");
		final byte[] bytes = byteArray.getRawArray();

		bytes[0] = 4;

		assertNotEquals(byteArray, ImmutableByteArray.from(bytes));
		assertEquals(byteArray, ImmutableByteArray.from("1, 2, 3"));
	}

	@Test
	public void getLengthIsCorrect() {
		final ImmutableByteArray byteArray = ImmutableByteArray.from("1, 2, 3");

		assertEquals(7, byteArray.length());
	}

	@Test
	public void getBytesBetweenReturnsCorrectSliceOfArray() {
		final ImmutableByteArray byteArray = ImmutableByteArray.from(new byte[]{1, 2, 3, 4, 5, 6});

		assertEquals(ImmutableByteArray.from(new byte[]{4, 5, 6}), byteArray.getBytesBetween(3, 6));
	}

	@Test
	public void getBytesBetweenThrowsWhenInclusiveStartIndexOutOfBounds() {
		final ImmutableByteArray byteArray = ImmutableByteArray.from(new byte[]{1, 2, 3, 4, 5, 6});

		assertThrows(IndexOutOfBoundsException.class, () -> byteArray.getBytesBetween(-1, 6));
		assertThrows(IndexOutOfBoundsException.class, () -> byteArray.getBytesBetween(9, 10));
	}

	@Test
	public void getBytesBetweenThrowsWhenExclusiveEndIndexOutOfBounds() {
		final ImmutableByteArray byteArray = ImmutableByteArray.from(new byte[]{1, 2, 3, 4, 5, 6});

		assertThrows(IndexOutOfBoundsException.class, () -> byteArray.getBytesBetween(0, 7));
		assertThrows(IndexOutOfBoundsException.class, () -> byteArray.getBytesBetween(0, -1));
	}

	@Test
	public void getBytesBetweenThrowsWhenStartIndexIsGreaterThanEndIndex() {
		final ImmutableByteArray byteArray = ImmutableByteArray.from(new byte[]{1, 2, 3, 4, 5, 6});

		assertThrows(IllegalArgumentException.class, () -> byteArray.getBytesBetween(3, 2));
	}
}
