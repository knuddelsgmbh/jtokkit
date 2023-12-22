package com.knuddels.jtokkit;

import com.knuddels.jtokkit.api.Encoding;
import com.knuddels.jtokkit.api.EncodingResult;
import com.knuddels.jtokkit.api.GptBytePairEncodingParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Collections.emptyList;
import static java.util.Objects.requireNonNull;

/**
 * Implementation of the byte pair encoding algorithm as used by the OpenAI tiktoken tokenizer.
 */
class GptBytePairEncoding implements Encoding {
    private final String name;
    private final Pattern pattern;
    private final TokenEncoder encoder;
    private final SpecialEncoder specialEncoder;
    private final Map<Integer, byte[]> encodedToDecoded;

    /**
     * Creates a new instance of {@link GptBytePairEncoding}.
     *
     * @param params the parameters to use for the encoding
     */
    GptBytePairEncoding(GptBytePairEncodingParams params) {
        this.name = params.getName();
        this.pattern = params.getPattern();
        this.encoder = new TokenEncoder(params.getEncoder());
        this.specialEncoder = new SpecialEncoder(params.getSpecialTokensEncoder());
        this.encodedToDecoded = new HashMap<>(params.getEncoder().size());
        params.getEncoder().forEach((k, v) -> encodedToDecoded.put(v, k));
    }

    @Override
    public List<Integer> encode(String text) {
        return encodeInternal(text, null).getTokens();
    }

    @Override
    public EncodingResult encode(String text, int maxTokenCount) {
        return encodeInternal(text, maxTokenCount);
    }

    private EncodingResult encodeInternal(String text, Integer maxTokenCount) {
        if (text == null) {
            return new EncodingResult(emptyList(), false);
        }

        specialEncoder.checkForSpecialTokens(text);

        return encodeOrdinaryInternal(text, maxTokenCount);
    }

    @Override
    public List<Integer> encodeOrdinary(String text) {
        return encodeOrdinaryInternal(text, null).getTokens();
    }

    @Override
    public EncodingResult encodeOrdinary(String text, int maxTokenCount) {
        return encodeOrdinaryInternal(text, maxTokenCount);
    }

    private EncodingResult encodeOrdinaryInternal(String text, Integer maxTokenCount) {
        if (text == null) {
            return new EncodingResult(emptyList(), false);
        }

        List<Integer> out = new ArrayList<>();
        int tokenCount = encodeOrdinaryInternal(text, maxTokenCount, out);
        assert maxTokenCount != null || tokenCount == out.size();

        if (maxTokenCount != null) {
            // Make sure we didn't break the multibyte character
            for (int tokensToRemove = 0; tokensToRemove <= out.size(); tokensToRemove++) {
                int size = out.size() - tokensToRemove;
                List<Integer> tokens = new ArrayList<>(size);
                for (int i = 0; i < size; i++) {
                    tokens.add(out.get(i));
                }
                String decoded = decode(tokens);
                if (text.startsWith(decoded)) {
                    // If decoded text is equal to the head of the original text, we can safely return the tokens
                    return new EncodingResult(tokens, text.length() > decoded.length());
                }
            }
        }

        return new EncodingResult(out, false);
    }

    int encodeOrdinaryInternal(String text, Integer maxTokenCount, List<Integer> out) {
        int tokenCount = 0;
        List<Integer> ranks = new ArrayList<>(); // reused to avoid allocations
        for (Matcher matcher = pattern.matcher(text); (maxTokenCount == null || tokenCount < maxTokenCount) && matcher.find(); ) {
            byte[] bytes = matcher.group().getBytes(UTF_8);
            tokenCount += encoder.addTokensAndGetCount(maxTokenCount, bytes, out, ranks);
        }
        return tokenCount;
    }

    @Override
    public int countTokens(String text) {
        return encode(text).size();
    }

    @Override
    public int countTokensOrdinary(String text) {
        return encodeOrdinary(text).size();
    }

    @Override
    public String decode(List<Integer> tokens) {
        return new String(decodeBytes(tokens), UTF_8);
    }

    @Override
    public byte[] decodeBytes(List<Integer> tokens) {
        List<Byte> out = new ArrayList<>();
        for (int token : tokens) {
            byte[] decodedToken = decodeToken(token);
            for (byte b : decodedToken) {
                out.add(b);
            }
        }

        byte[] outArray = new byte[out.size()];
        for (int i = 0; i < out.size(); i++) {
            outArray[i] = out.get(i);
        }
        return outArray;
    }

    @Override
    public String getName() {
        return name;
    }

    private byte[] decodeToken(int token) {
        byte[] decodedToken = encodedToDecoded.computeIfAbsent(token, specialEncoder::decodeIfPresent);
        return requireNonNull(decodedToken, "Unknown token for decoding: " + token);
    }
}
