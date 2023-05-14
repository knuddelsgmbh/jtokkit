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
	 * Creates a new {@link EncodingRegistry} without any {@link EncodingType} registered.
	 *
	 * @return the new {@link EncodingRegistry}
	 */
	public static EncodingRegistry newLazyEncodingRegistry() {
		return new LazyEncodingRegistry();
	}
	
	private Encodings() {
	}
}
