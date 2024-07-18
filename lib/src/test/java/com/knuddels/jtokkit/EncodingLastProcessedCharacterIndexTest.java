package com.knuddels.jtokkit;

import com.knuddels.jtokkit.api.Encoding;
import com.knuddels.jtokkit.api.EncodingType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EncodingLastProcessedCharacterIndexTest {


    private static final Encoding ENCODING = Encodings.newDefaultEncodingRegistry().getEncoding(EncodingType.CL100K_BASE);

    @Test
    void testNullInput() {
        var encodingResult = ENCODING.encode(null, 10);
        assertEquals(encodingResult.getLastProcessedCharacterIndex(), -1);
    }

    @Test
    void testEmptyInput() {
        String input = "";
        var encodingResult = ENCODING.encode(input, 10);
        assertEquals(encodingResult.getLastProcessedCharacterIndex(), -1);
    }

    @Test
    void testShortInput() {
        String input = "Hello World!";
        var encodingResult = ENCODING.encode(input, 10);
        assertEquals(encodingResult.getLastProcessedCharacterIndex(), 11);
    }

    @Test
    void testLongInput() {
        String input = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce condimentum enim ac tellus malesuada, a consectetur nibh efficitur. 🚀🚀🚀";
        var encodingResult = ENCODING.encode(input, 10);
        assertEquals(encodingResult.getLastProcessedCharacterIndex(), 55);
    }
}
