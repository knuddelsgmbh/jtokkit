package com.knuddels.jtokkit;

import com.knuddels.jtokkit.api.Encoding;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import static com.knuddels.jtokkit.TokenEncoder.VERY_LARGE_TOKENIZER_BYTE_THRESHOLD_KEY;

class Cl100kLargeTokenizerTest extends Cl100kTest {

    public static Encoding ENCODING;

    @BeforeAll
    static void beforeAll() {
        System.setProperty(VERY_LARGE_TOKENIZER_BYTE_THRESHOLD_KEY, String.valueOf(0));
        ENCODING = EncodingFactory.cl100kBase();
    }

    @AfterAll
    static void afterAll() {
        System.clearProperty(VERY_LARGE_TOKENIZER_BYTE_THRESHOLD_KEY);

    }

    Encoding getEncoding() {
        return ENCODING;
    }
}
