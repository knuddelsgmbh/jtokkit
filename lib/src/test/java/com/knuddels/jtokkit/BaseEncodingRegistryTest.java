package com.knuddels.jtokkit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.knuddels.jtokkit.api.Encoding;
import com.knuddels.jtokkit.api.EncodingResult;
import com.knuddels.jtokkit.api.EncodingType;
import com.knuddels.jtokkit.api.GptBytePairEncodingParams;
import com.knuddels.jtokkit.api.IntArrayList;
import com.knuddels.jtokkit.api.ModelType;
import java.util.Collections;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

abstract class BaseEncodingRegistryTest<T extends AbstractEncodingRegistry> {

    protected final T registry;
    protected final Consumer<T> initializer;

    BaseEncodingRegistryTest(T registry) {
        this(registry, __ -> {
        });
    }

    BaseEncodingRegistryTest(T registry, Consumer<T> initializer) {
        this.registry = registry;
        this.initializer = initializer;
    }

    @BeforeEach
    void setup() {
        initializer.accept(registry);
    }

    @Test
    void getEncodingReturnsCorrectEncoding() {
        for (var type : EncodingType.values()) {
            var encoding = registry.getEncoding(type);
            assertNotNull(encoding);
            assertEquals(type.getName(), encoding.getName());
        }
    }

    @Test
    void getEncodingByNameReturnsCorrectEncoding() {
        for (var type : EncodingType.values()) {
            var encoding = registry.getEncoding(type.getName());
            assertTrue(encoding.isPresent());
            assertEquals(encoding.get().getName(), type.getName());
        }
    }

    @Test
    void getEncodingForModelReturnsCorrectsEncoding() {
        for (var modelType : ModelType.values()) {
            var encoding = registry.getEncodingForModel(modelType);
            assertNotNull(encoding);
            assertEquals(modelType.getEncodingType().getName(), encoding.getName());
        }
    }

    @Test
    void getEncodingForModelByNameReturnsCorrectEncoding() {
        for (var modelType : ModelType.values()) {
            var encoding = registry.getEncodingForModel(modelType.getName());
            assertTrue(encoding.isPresent());
            assertEquals(encoding.get().getName(), modelType.getEncodingType().getName());
        }
    }

    @Test
    void getEncodingForModelByPrefixReturnsCorrectEncodingForGpt432k() {
        var encoding = registry.getEncodingForModel("gpt-4-32k-0314");
        assertTrue(encoding.isPresent());
        assertEquals(encoding.get().getName(), ModelType.GPT_4.getEncodingType().getName());
    }

    @Test
    void getEncodingForModelByPrefixReturnsCorrectEncodingForGpt4() {
        var encoding = registry.getEncodingForModel("gpt-4-0314");
        assertTrue(encoding.isPresent());
        assertEquals(encoding.get().getName(), ModelType.GPT_4.getEncodingType().getName());
    }

    @Test
    void getEncodingForModelByPrefixReturnsCorrectEncodingForGpt4o() {
        var encoding = registry.getEncodingForModel("gpt-4o-123");
        assertTrue(encoding.isPresent());
        assertEquals(encoding.get().getName(), ModelType.GPT_4O.getEncodingType().getName());
    }

    @Test
    void getEncodingForModelByPrefixReturnsCorrectEncodingForGpt4oMini() {
        var encoding = registry.getEncodingForModel("gpt-4o-mini-123");
        assertTrue(encoding.isPresent());
        assertEquals(encoding.get().getName(), ModelType.GPT_4O_MINI.getEncodingType().getName());
    }

    @Test
    void getEncodingForModelByPrefixReturnsCorrectEncodingForGpt4Turbo() {
        var encoding = registry.getEncodingForModel("gpt-4-turbo-123");
        assertTrue(encoding.isPresent());
        assertEquals(encoding.get().getName(), ModelType.GPT_4_TURBO.getEncodingType().getName());
    }

    @Test
    void getEncodingForModelByPrefixReturnsCorrectEncodingForGpt3Turbo() {
        var encoding = registry.getEncodingForModel("gpt-3.5-turbo-0301");
        assertTrue(encoding.isPresent());
        assertEquals(encoding.get().getName(), ModelType.GPT_3_5_TURBO.getEncodingType().getName());
    }

    @Test
    void getEncodingForModelByPrefixReturnsCorrectEncodingForGpt3Turbo16k() {
        var encoding = registry.getEncodingForModel("gpt-3.5-turbo-16k-0613");
        assertTrue(encoding.isPresent());
        assertEquals(encoding.get().getName(), ModelType.GPT_3_5_TURBO_16K.getEncodingType().getName());
    }

    @Test
    void canRegisterCustomEncoding() {
        Encoding encoding = new DummyEncoding();

        registry.registerCustomEncoding(encoding);

        var retrievedEncoding = registry.getEncoding(encoding.getName());

        assertTrue(retrievedEncoding.isPresent());
        assertEquals(encoding, retrievedEncoding.get());
    }

    @Test
    void canRegisterCustomGptBpe() {
        var params = new GptBytePairEncodingParams(
                "custom",
                Pattern.compile("test"),
                Collections.emptyMap(),
                Collections.emptyMap()
        );

        registry.registerGptBytePairEncoding(params);

        var retrievedEncoding = registry.getEncoding(params.getName());

        assertTrue(retrievedEncoding.isPresent());
        assertEquals(params.getName(), retrievedEncoding.get().getName());
    }

    @Test
    void throwsIfCustomEncodingIsAlreadyRegistered() {
        Encoding encoding = new DummyEncoding();

        registry.registerCustomEncoding(encoding);

        assertThrows(IllegalStateException.class, () -> registry.registerCustomEncoding(encoding));
    }

    @Test
    void getEncodingReturnsEmptyOptionalForNonExistingEncodingName() {
        var result = registry.getEncoding("nonexistent");

        assertFalse(result.isPresent());
    }

    private static class DummyEncoding implements Encoding {

        @Override
        public IntArrayList encode(String text) {
            return null;
        }

        @Override
        public EncodingResult encode(String text, int maxTokens) {
            return null;
        }

        @Override
        public IntArrayList encodeOrdinary(String text) {
            return null;
        }

        @Override
        public EncodingResult encodeOrdinary(String text, int maxTokens) {
            return null;
        }

        @Override
        public int countTokens(String text) {
            return 0;
        }

        @Override
        public int countTokensOrdinary(final String text) {
            return 0;
        }

        @Override
        public String decode(IntArrayList tokens) {
            return null;
        }

        @Override
        public byte[] decodeBytes(IntArrayList tokens) {
            return new byte[0];
        }

        @Override
        public String getName() {
            return "dummy";
        }
    }
}
