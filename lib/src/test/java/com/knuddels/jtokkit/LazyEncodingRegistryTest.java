package com.knuddels.jtokkit;

import com.knuddels.jtokkit.api.Encoding;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.assertTrue;

class LazyEncodingRegistryTest extends BaseEncodingRegistryTest {

	@BeforeEach
	void initializeLazyEncodingRegistry() {
		registry = new LazyEncodingRegistry();
	}

	@Order(1)
	@Test
	public void initializeWithEmptyEncoding() throws NoSuchFieldException, IllegalAccessException {
		Field field = AbstractEncodingRegistry.class.getDeclaredField("encodings");
		ConcurrentHashMap<String, Encoding> encodings = (ConcurrentHashMap<String, Encoding>) field.get(registry);
		assertTrue(encodings.isEmpty());
	}
}
