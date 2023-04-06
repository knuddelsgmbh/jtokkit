package com.knuddels.jtokkit;

import com.knuddels.jtokkit.api.*;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Thread-safe default implementation of {@link EncodingRegistry}. During initialization, it registers the default encodings
 * for the different {@link EncodingType}s.
 */
final class DefaultEncodingRegistry implements EncodingRegistry {
	private final ConcurrentHashMap<String, Encoding> encodings = new ConcurrentHashMap<>();

	/**
	 * Initializes the registry with the default encodings.
	 *
	 * @throws IllegalStateException if an unknown encoding type is encountered
	 */
	public void initializeDefaultEncodings() {
		for (final EncodingType encodingType : EncodingType.values()) {
			switch (encodingType) {
				case R50K_BASE:
					encodings.put(encodingType.getName(), EncodingFactory.r50kBase());
					break;
				case P50K_BASE:
					encodings.put(encodingType.getName(), EncodingFactory.p50kBase());
					break;
				case P50K_EDIT:
					encodings.put(encodingType.getName(), EncodingFactory.p50kEdit());
					break;
				case CL100K_BASE:
					encodings.put(encodingType.getName(), EncodingFactory.cl100kBase());
					break;
				default:
					throw new IllegalStateException("Unknown encoding type " + encodingType.getName());
			}
		}
	}

	@Override
	public Optional<Encoding> getEncoding(final String encodingName) {
		return Optional.ofNullable(encodings.get(encodingName));
	}

	@Override
	public Encoding getEncoding(final EncodingType encodingType) {
		return Objects.requireNonNull(
				encodings.get(encodingType.getName()),
				() -> "No encoding registered for encoding type " + encodingType.getName()
		);
	}

	@Override
	public Optional<Encoding> getEncodingForModel(final String modelName) {
		return ModelType.fromName(modelName)
				.map(this::getEncodingForModel);
	}

	@Override
	public Encoding getEncodingForModel(final ModelType modelType) {
		return Objects.requireNonNull(
				encodings.get(modelType.getEncodingType().getName()),
				() -> "No encoding registered for model type " + modelType.getName()
		);
	}

	@Override
	public EncodingRegistry registerGptBytePairEncoding(final GptBytePairEncodingParams parameters) {
		return registerCustomEncoding(EncodingFactory.fromParameters(parameters));
	}

	@Override
	public EncodingRegistry registerCustomEncoding(final Encoding encoding) {
		final String encodingName = encoding.getName();
		final Encoding previousEncoding = encodings.putIfAbsent(encodingName, encoding);
		if (previousEncoding != null) {
			throw new IllegalStateException("Encoding " + encodingName + " already registered");
		}

		return this;
	}
}
