package com.knuddels.jtokkit;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * A TokenEncoder is used to encode and decode tokens. It is initialized with a map
 * that contains the decoded tokens as keys and the encoded tokens as values. The
 * TokenEncoder can then be used to encode and decode tokens.
 */
class TokenEncoder {

    private final Map<ImmutableByteArray, Integer> decodedToEncoded = new HashMap<>();

    /**
     * Creates a new TokenEncoder with the given input map. The keys of the map are
     * the decoded tokens and the values are the encoded tokens.
     *
     * @param input the input map
     */
    public TokenEncoder(Map<ImmutableByteArray, Integer> input) {
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
    public <T> TokenEncoder(Map<T, Integer> input, Function<T, ImmutableByteArray> keyMapper) {
        for (Map.Entry<T, Integer> entry : input.entrySet()) {
            ImmutableByteArray key = keyMapper.apply(entry.getKey());
            Integer value = entry.getValue();
            decodedToEncoded.put(key, value);
        }
    }

    /**
     * Checks if the given decoded token is contained in this encoder.
     *
     * @param decodedToken the decoded token
     * @return true if the decoded token is contained in this encoder, false otherwise
     */
    public boolean containsDecodedToken(ImmutableByteArray decodedToken) {
        return decodedToEncoded.containsKey(decodedToken);
    }

    /**
     * Encodes the given decoded token.
     *
     * @param decodedToken the decoded token
     * @return the encoded token
     * @throws IllegalArgumentException if the decoded token is not contained in this encoder
     */
    public Integer encode(ImmutableByteArray decodedToken) {
        Integer encoded = decodedToEncoded.get(decodedToken);
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
    public Optional<Integer> encodeIfPresent(ImmutableByteArray decodedToken) {
        return Optional.ofNullable(decodedToEncoded.get(decodedToken));
    }
}
