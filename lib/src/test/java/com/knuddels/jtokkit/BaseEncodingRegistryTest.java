package com.knuddels.jtokkit;

import com.knuddels.jtokkit.api.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

public abstract class BaseEncodingRegistryTest<T extends AbstractEncodingRegistry> {

    protected final T registry;
    protected final Consumer<T> initializer;

    public BaseEncodingRegistryTest(final T registry) {
        this(registry, __ -> {});
    }

    public BaseEncodingRegistryTest(final T registry, final Consumer<T> initializer) {
        this.registry = registry;
        this.initializer = initializer;
    }

    @BeforeEach
    public void setup() {
        initializer.accept(registry);
    }

    @Test
    public void getEncodingReturnsCorrectEncoding() {
        for (final EncodingType type : EncodingType.values()) {
            final Encoding encoding = registry.getEncoding(type);
            assertNotNull(encoding);
            assertEquals(type.getName(), encoding.getName());
        }
    }

    @Test
    void getEncodingByNameReturnsCorrectEncoding() {
        for (final EncodingType type : EncodingType.values()) {
            final Optional<Encoding> encoding = registry.getEncoding(type.getName());
            assertTrue(encoding.isPresent());
            assertEquals(encoding.get().getName(), type.getName());
        }
    }

    @Test
    public void getEncodingForModelReturnsCorrectsEncoding() {
        for (final ModelType modelType : ModelType.values()) {
            final Encoding encoding = registry.getEncodingForModel(modelType);
            assertNotNull(encoding);
            assertEquals(modelType.getEncodingType().getName(), encoding.getName());
        }
    }

    @Test
    public void getEncodingForModelByNameReturnsCorrectEncoding() {
        for (final ModelType modelType : ModelType.values()) {
            final Optional<Encoding> encoding = registry.getEncodingForModel(modelType.getName());
            assertTrue(encoding.isPresent());
            assertEquals(encoding.get().getName(), modelType.getEncodingType().getName());
        }
    }

    @Test
    public void getEncodingForModelByPrefixReturnsCorrectEncodingForGpt432k() {
        final Optional<Encoding> encoding = registry.getEncodingForModel("gpt-4-32k-0314");
        assertTrue(encoding.isPresent());
        assertEquals(encoding.get().getName(), ModelType.GPT_4.getEncodingType().getName());
    }

    @Test
    public void getEncodingForModelByPrefixReturnsCorrectEncodingForGpt4() {
        final Optional<Encoding> encoding = registry.getEncodingForModel("gpt-4-0314");
        assertTrue(encoding.isPresent());
        assertEquals(encoding.get().getName(), ModelType.GPT_4.getEncodingType().getName());
    }

    @Test
    public void getEncodingForModelByPrefixReturnsCorrectEncodingForGpt3Turbo() {
        final Optional<Encoding> encoding = registry.getEncodingForModel("gpt-3.5-turbo-0301");
        assertTrue(encoding.isPresent());
        assertEquals(encoding.get().getName(), ModelType.GPT_3_5_TURBO.getEncodingType().getName());
    }

    @Test
    public void canRegisterCustomEncoding() {
        final Encoding encoding = new DummyEncoding();

        registry.registerCustomEncoding(encoding);

        final Optional<Encoding> retrievedEncoding = registry.getEncoding(encoding.getName());

        assertTrue(retrievedEncoding.isPresent());
        assertEquals(encoding, retrievedEncoding.get());
    }

    @Test
    public void canRegisterCustomGptBpe() {
        final GptBytePairEncodingParams params = new GptBytePairEncodingParams(
                "custom",
                Pattern.compile("test"),
                Collections.emptyMap(),
                Collections.emptyMap()
        );

        registry.registerGptBytePairEncoding(params);

        final Optional<Encoding> retrievedEncoding = registry.getEncoding(params.getName());

        assertTrue(retrievedEncoding.isPresent());
        assertEquals(params.getName(), retrievedEncoding.get().getName());
    }

    @Test
    public void throwsIfCustomEncodingIsAlreadyRegistered() {
        final Encoding encoding = new DummyEncoding();

        registry.registerCustomEncoding(encoding);

        assertThrows(IllegalStateException.class, () -> registry.registerCustomEncoding(encoding));
    }

    @Test
    public void getEncodingReturnsEmptyOptionalForNonExistingEncodingName() {
        final Optional<Encoding> result = registry.getEncoding("nonexistent");

        assertFalse(result.isPresent());
    }

    private static class DummyEncoding implements Encoding {

        @Override
        public List<Integer> encode(final String text) {
            return null;
        }

        @Override
        public EncodingResult encode(final String text, final int maxTokens) {
            return null;
        }

        @Override
        public List<Integer> encodeOrdinary(final String text) {
            return null;
        }

        @Override
        public EncodingResult encodeOrdinary(final String text, final int maxTokens) {
            return null;
        }

        @Override
        public int countTokens(final String text) {
            return 0;
        }

        @Override
        public int countTokensOrdinary(final String text) {
            return 0;
        }

        @Override
        public String decode(final List<Integer> tokens) {
            return null;
        }

        @Override
        public byte[] decodeBytes(final List<Integer> tokens) {
            return new byte[0];
        }

        @Override
        public String getName() {
            return "dummy";
        }
    }
}
