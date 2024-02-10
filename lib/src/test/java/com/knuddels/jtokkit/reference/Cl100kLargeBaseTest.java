package com.knuddels.jtokkit.reference;

import com.knuddels.jtokkit.Encodings;
import com.knuddels.jtokkit.api.Encoding;
import com.knuddels.jtokkit.api.EncodingType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

class Cl100kLargeBaseTest extends Cl100kBaseTest {

    private static Encoding ENCODING;

    @BeforeAll
    static void beforeAll() {
        System.setProperty(Encoding.VERY_LARGE_TOKENIZER_BYTE_THRESHOLD_KEY, String.valueOf(0));
        ENCODING = Encodings.newDefaultEncodingRegistry().getEncoding(EncodingType.CL100K_BASE);
    }

    @AfterAll
    static void afterAll() {
        System.clearProperty(Encoding.VERY_LARGE_TOKENIZER_BYTE_THRESHOLD_KEY);

    }

    Encoding getEncoding() {
        return ENCODING;
    }
}
