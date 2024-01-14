package com.knuddels.jtokkit;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class ByteArrayListTest {

    private static byte randomByte(Random random) {
        return (byte) (random.nextInt() & 0xFF);
    }

    @Test
    void testArrayListOperations() {
        var byteArrayList = new ByteArrayList();
        var standardList = new ArrayList<Byte>();
        var random = new Random();

        assertTrue(byteArrayList.isEmpty());

        for (var i = 0; i < 1_000; i++) {
            // Add
            var element = randomByte(random);
            byteArrayList.add(element);
            standardList.add(element);
            assertEquals(standardList.get(standardList.size() - 1), byteArrayList.get(byteArrayList.size() - 1));

            // Set
            if (!byteArrayList.isEmpty() && random.nextBoolean()) {
                var randomIndex = random.nextInt(byteArrayList.size());
                var newElement = randomByte(random);
                byteArrayList.set(randomIndex, newElement);
                standardList.set(randomIndex, newElement);
                assertEquals(standardList.get(randomIndex), byteArrayList.get(randomIndex));
            }

            // Size and IsEmpty
            assertEquals(standardList.size(), byteArrayList.size());
            assertEquals(standardList.isEmpty(), byteArrayList.isEmpty());

            // Boxed and ToString
            assertEquals(standardList, byteArrayList.boxed());
            assertEquals(standardList.toString(), byteArrayList.toString());

            // Clear
            if (randomByte(random) % 10 == 0) {
                byteArrayList.clear();
                standardList.clear();
                assertEquals(standardList.size(), byteArrayList.size());
            }
        }
        var element = randomByte(random);
        byteArrayList.add(element);
        standardList.add(element);


        // Test toArray
        var byteArray = byteArrayList.toArray();
        assertEquals(standardList.size(), byteArray.length);
        for (var i = 0; i < byteArrayList.size(); i++) {
            assertEquals(standardList.get(i), byteArray[i]);
        }

        // Test Equals and HashCode
        var anotherByteArrayList = new ByteArrayList();
        standardList.forEach(anotherByteArrayList::add);

        assertEquals(byteArrayList, byteArrayList);
        assertEquals(byteArrayList, anotherByteArrayList);
        assertEquals(byteArrayList.hashCode(), anotherByteArrayList.hashCode());

        assertNotEquals(byteArrayList, new Object());
        anotherByteArrayList.set(0, (byte) (byteArrayList.get(0) + 1));
        assertNotEquals(byteArrayList, anotherByteArrayList);

        assertNotEquals(byteArrayList, new ByteArrayList());
    }
}
