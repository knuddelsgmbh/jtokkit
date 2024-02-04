package com.knuddels.jtokkit;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.knuddels.jtokkit.api.Encoding;
import java.util.concurrent.ConcurrentHashMap;
import org.junit.jupiter.api.Test;

class LazyEncodingRegistryTest extends BaseEncodingRegistryTest<LazyEncodingRegistry> {

    LazyEncodingRegistryTest() {
        super(new LazyEncodingRegistry());
    }

    @Test
    @SuppressWarnings("unchecked")
    void initializeWithEmptyEncoding() throws NoSuchFieldException, IllegalAccessException {
        var field = AbstractEncodingRegistry.class.getDeclaredField("encodings");
        field.setAccessible(true);
        var encodings = (ConcurrentHashMap<String, Encoding>) field.get(registry);
        assertTrue(encodings.isEmpty());
    }
}
