package com.knuddels.jtokkit;

import java.util.*;
import java.util.function.Function;

/**
 * A TokenEncoder is used to encode and decode tokens. It is initialized with a map
 * that contains the decoded tokens as keys and the encoded tokens as values. The
 * TokenEncoder can then be used to encode and decode tokens.
 *
 * @param <K> the type of the decoded tokens
 * @param <V> the type of the encoded tokens
 */
final class TokenEncoder<K, V> {

	private final Map<K, V> decodedToEncoded = new HashMap<>();
	private final Map<V, K> encodedToDecoded = new HashMap<>();

	/**
	 * Creates a new TokenEncoder with the given input map. The keys of the map are
	 * the decoded tokens and the values are the encoded tokens.
	 *
	 * @param input the input map
	 */
	public TokenEncoder(final Map<K, V> input) {
		this(input, Function.identity());
	}

	/**
	 * Creates a new TokenEncoder with the given input map. The keys of the map are
	 * the decoded tokens and the values are the encoded tokens. The keyMapper is
	 * applied to the keys of the input map before they are added to the internal
	 * maps.
	 *
	 * @param input     the input map
	 * @param keyMapper the key mapper
	 */
	public <T> TokenEncoder(final Map<T, V> input, final Function<T, K> keyMapper) {
		for (final Map.Entry<T, V> entry : input.entrySet()) {
			final K key = keyMapper.apply(entry.getKey());
			final V value = entry.getValue();
			decodedToEncoded.put(key, value);
			encodedToDecoded.put(value, key);
		}
	}

	/**
	 * Checks if the given decoded token is contained in this encoder.
	 *
	 * @param decodedToken the decoded token
	 * @return true if the decoded token is contained in this encoder, false otherwise
	 */
	public boolean containsDecodedToken(final K decodedToken) {
		return decodedToEncoded.containsKey(decodedToken);
	}

	/**
	 * Encodes the given decoded token.
	 *
	 * @param decodedToken the decoded token
	 * @return the encoded token
	 * @throws IllegalArgumentException if the decoded token is not contained in this encoder
	 */
	public V encode(final K decodedToken) {
		final V encoded = decodedToEncoded.get(decodedToken);
		if (encoded == null) {
			throw new IllegalArgumentException("Unknown token for encoding: " + decodedToken);
		}

		return encoded;
	}

	/**
	 * Encodes the given decoded token if it is contained in this encoder. Otherwise,
	 * an empty optional is returned.
	 *
	 * @param decodedToken the decoded token
	 * @return the encoded token or an empty optional
	 */
	public Optional<V> encodeIfPresent(final K decodedToken) {
		return Optional.ofNullable(decodedToEncoded.get(decodedToken));
	}

	/**
	 * Decodes the given encoded token if it is contained in this encoder. Otherwise,
	 * an empty optional is returned.
	 *
	 * @param encodedToken the encoded token
	 * @return the decoded token or an empty optional
	 */
	public Optional<K> decodeIfPresent(final V encodedToken) {
		return Optional.ofNullable(encodedToDecoded.get(encodedToken));
	}

	/**
	 * Returns an unmodifiable set of all decoded tokens contained in this encoder.
	 *
	 * @return an unmodifiable set of all decoded tokens
	 */
	public Set<K> getDecodedTokens() {
		return Collections.unmodifiableSet(decodedToEncoded.keySet());
	}
}
