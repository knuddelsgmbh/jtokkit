package com.knuddels.jtokkit;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.nio.charset.StandardCharsets.UTF_8;

final class SpecialEncoder {
    private static final String SPECIAL_START = "<|";
    private static final String SPECIAL_END = "|>";
    private final Map<Integer, String> encodedToDecoded;

    public SpecialEncoder(Map<String, Integer> encoder) {
        this.encodedToDecoded = new ConcurrentHashMap<>(encoder.size());
        for (Map.Entry<String, Integer> entry : encoder.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();

            assert key.contains(SPECIAL_START) && key.contains(SPECIAL_END) : "Special tokens must contain <| and |> (but was " + key + ")";

            encodedToDecoded.put(value, key);
        }
    }

    public byte[] decodeIfPresent(Integer encodedToken) {
        String result = encodedToDecoded.get(encodedToken);
        return result != null ? result.getBytes(UTF_8) : null;
    }

    public void checkForSpecialTokens(String text) {
        if (text.contains(SPECIAL_START) && text.contains(SPECIAL_END)) {
            for (String specialToken : encodedToDecoded.values()) {
                if (text.contains(specialToken)) {
                    throw new UnsupportedOperationException("Encoding special tokens is not supported yet.");
                }
            }
        }
    }
}
