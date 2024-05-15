package com.knuddels.jtokkit;

import com.knuddels.jtokkit.api.Encoding;
import com.knuddels.jtokkit.api.EncodingType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EncodingResultLastTokenIndexTest {

    private static final Encoding ENCODING = Encodings.newDefaultEncodingRegistry().getEncoding(EncodingType.R50K_BASE);

    @Test
    void encodingResultLastTokenIndexTest() {
        String input = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce condimentum enim ac tellus malesuada, a consectetur nibh efficitur. 🚀🚀🚀";
        var encodingResult = ENCODING.encode(input, 10);
        assertEquals(encodingResult.getLastTokenIndex(), 25);
    }
}
