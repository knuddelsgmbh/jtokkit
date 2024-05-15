package com.knuddels.jtokkit;

import com.knuddels.jtokkit.api.Encoding;
import com.knuddels.jtokkit.api.EncodingType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EncodingResultLastTokenIndexTest {

    private static final Encoding ENCODING = Encodings.newDefaultEncodingRegistry().getEncoding(EncodingType.R50K_BASE);

    @Test
    void testNoneInput() {
        String input = "";
        var encodingResult = ENCODING.encode(input, 10);
        assertEquals(encodingResult.getLastTokenIndex(), -1);
    }

    @Test
    void testShortInput() {
        String input = "Hello World!";
        var encodingResult = ENCODING.encode(input, 10);
        assertEquals(encodingResult.getLastTokenIndex(), 11);
    }

    @Test
    void testLongInput() {
        String input = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce condimentum enim ac tellus malesuada, a consectetur nibh efficitur. 🚀🚀🚀";
        var encodingResult = ENCODING.encode(input, 10);
        assertEquals(encodingResult.getLastTokenIndex(), 25);
    }
}
