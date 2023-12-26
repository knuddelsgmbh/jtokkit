package com.knuddels.jtokkit;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ByteArrayListTest {

    private static byte randomByte(Random random) {
        return (byte) (random.nextInt() & 0xFF);
    }

    @Test
    public void testArrayListOperations() {
        var byteArrayList = new ByteArrayList();
        var standardList = new ArrayList<Byte>();
        var random = new Random();

        for (var i = 0; i < 1_000; i++) {
            // Add
            if (randomByte(random) % 2 == 0) {
                var element = randomByte(random);
                var lastIndex = standardList.size();
                byteArrayList.add(element);
                standardList.add(element);
                assertEquals(standardList.get(lastIndex), byteArrayList.toByteArray()[lastIndex]);
            }

            // Size
            assertEquals(standardList.size(), byteArrayList.length());

            // Clear
            if (randomByte(random) % 10 == 0) {
                byteArrayList.clear();
                standardList.clear();
                assertEquals(standardList.size(), byteArrayList.length());
            }
        }

        assertEquals(standardList.size(), byteArrayList.length());
        var byteArray = byteArrayList.toByteArray();
        assertEquals(standardList.size(), byteArray.length);
        for (var i = 0; i < byteArrayList.length(); i++) {
            assertEquals(standardList.get(i), byteArray[i]);
        }
    }
}
