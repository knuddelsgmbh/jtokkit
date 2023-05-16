package com.knuddels.jtokkit;

import com.knuddels.jtokkit.api.EncodingRegistry;
import com.knuddels.jtokkit.api.EncodingType;

public final class Encodings {

	/**
	 * Creates a new {@link EncodingRegistry} with the default encodings found in the {@link EncodingType} enum.
	 *
	 * @return the new {@link EncodingRegistry}
	 */
	public static EncodingRegistry newDefaultEncodingRegistry() {
		final DefaultEncodingRegistry registry = new DefaultEncodingRegistry();
		registry.initializeDefaultEncodings();
		return registry;
	}
	
	/**
	 * Creates a new {@link EncodingRegistry} without any {@link EncodingType} registered. Encodings will be
	 * loaded on-demand when they are first requested. For example, if you call
	 * {@link EncodingRegistry#getEncoding(EncodingType)} with {@link EncodingType#CL100K_BASE} for the first time,
	 * it will be loaded from the classpath. Subsequent calls with {@link EncodingType#CL100K_BASE} will re-use the
	 * already loaded encoded.
	 *
	 * @return the new {@link EncodingRegistry}
	 */
	public static EncodingRegistry newLazyEncodingRegistry() {
		return new LazyEncodingRegistry();
	}
	
	private Encodings() {
	}
}
