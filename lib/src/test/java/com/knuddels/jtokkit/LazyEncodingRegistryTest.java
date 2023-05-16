package com.knuddels.jtokkit;

import com.knuddels.jtokkit.api.Encoding;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LazyEncodingRegistryTest extends BaseEncodingRegistryTest<LazyEncodingRegistry> {

	public LazyEncodingRegistryTest() {
		super(new LazyEncodingRegistry());
	}

	@Test
	@SuppressWarnings("unchecked")
	public void initializeWithEmptyEncoding() throws NoSuchFieldException, IllegalAccessException {
		final Field field = AbstractEncodingRegistry.class.getDeclaredField("encodings");
		field.setAccessible(true);
		final ConcurrentHashMap<String, Encoding> encodings = (ConcurrentHashMap<String, Encoding>) field.get(registry);
		assertTrue(encodings.isEmpty());
	}
}
