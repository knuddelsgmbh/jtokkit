package com.knuddels.jtokkit;

import com.knuddels.jtokkit.api.IntArrayList;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class IntArrayListTest {

    @Test
    void testArrayListOperations() {
        var byteArrayList = new IntArrayList();
        var standardList = new ArrayList<Integer>();
        var random = new Random();

        assertTrue(byteArrayList.isEmpty());

        for (var i = 0; i < 1_000; i++) {
            // Add
            var element = random.nextInt();
            byteArrayList.add(element);
            standardList.add(element);
            assertEquals(standardList.get(standardList.size() - 1), byteArrayList.get(byteArrayList.size() - 1));

            // Set
            if (!byteArrayList.isEmpty() && random.nextBoolean()) {
                var randomIndex = random.nextInt(byteArrayList.size());
                var newElement = random.nextInt();
                byteArrayList.set(randomIndex, newElement);
                standardList.set(randomIndex, newElement);
                assertEquals(standardList.get(randomIndex), byteArrayList.get(randomIndex));
            }

            // Size and IsEmpty
            assertEquals(standardList.size(), byteArrayList.size());
            assertEquals(standardList.isEmpty(), byteArrayList.isEmpty());

            // Clear
            if (random.nextInt() % 10 == 0) {
                byteArrayList.clear();
                standardList.clear();
                assertEquals(standardList.size(), byteArrayList.size());
            }
        }

        // Test toArray
        var byteArray = byteArrayList.toArray();
        assertEquals(standardList.size(), byteArray.length);
        for (var i = 0; i < byteArrayList.size(); i++) {
            assertEquals(standardList.get(i), byteArray[i]);
        }

        // Test Equals and HashCode
        var anotherIntArrayList = new IntArrayList();
        standardList.forEach(anotherIntArrayList::add);

        assertEquals(byteArrayList, anotherIntArrayList);
        if (!byteArrayList.isEmpty()) {
            assertNotEquals(byteArrayList, new IntArrayList());
        }
        assertEquals(byteArrayList.hashCode(), anotherIntArrayList.hashCode());
    }
}
