package com.knuddels.jtokkit.reference;

import static com.knuddels.jtokkit.TokenEncoder.VERY_LARGE_TOKENIZER_BYTE_THRESHOLD_KEY;

import com.knuddels.jtokkit.Encodings;
import com.knuddels.jtokkit.api.Encoding;
import com.knuddels.jtokkit.api.EncodingType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

class Cl100kLargeBaseTest extends Cl100kBaseTest {

    public static Encoding ENCODING;

    @BeforeAll
    static void beforeAll() {
        System.setProperty(VERY_LARGE_TOKENIZER_BYTE_THRESHOLD_KEY, String.valueOf(0));
        ENCODING = Encodings.newDefaultEncodingRegistry().getEncoding(EncodingType.CL100K_BASE);
    }

    @AfterAll
    static void afterAll() {
        System.clearProperty(VERY_LARGE_TOKENIZER_BYTE_THRESHOLD_KEY);

    }

    Encoding getEncoding() {
        return ENCODING;
    }
}
