package com.knuddels.jtokkit;

import com.knuddels.jtokkit.api.Encoding;
import com.knuddels.jtokkit.api.GptBytePairEncodingParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class EncodingFactory {
    private static final String ENDOFTEXT = "<|endoftext|>";
    private static final String FIM_PREFIX = "<|fim_prefix|>";
    private static final String FIM_MIDDLE = "<|fim_middle|>";
    private static final String FIM_SUFFIX = "<|fim_suffix|>";
    private static final String ENDOFPROMPT = "<|endofprompt|>";

    private static final Map<String, Integer> SPECIAL_TOKENS_X50K_BASE;
    private static final Map<String, Integer> SPECIAL_TOKENS_P50K_EDIT;
    private static final Map<String, Integer> SPECIAL_TOKENS_CL100K_BASE;

    static {
        Map<String, Integer> map = new HashMap<>();
        map.put(ENDOFTEXT, 50256);
        SPECIAL_TOKENS_X50K_BASE = Collections.unmodifiableMap(map);
    }

    static {
        Map<String, Integer> map = new HashMap<>();
        map.put(ENDOFTEXT, 50256);
        map.put(FIM_PREFIX, 50281);
        map.put(FIM_MIDDLE, 50282);
        map.put(FIM_SUFFIX, 50283);
        SPECIAL_TOKENS_P50K_EDIT = Collections.unmodifiableMap(map);
    }

    static {
        Map<String, Integer> map = new HashMap<>();
        map.put(ENDOFTEXT, 100257);
        map.put(FIM_PREFIX, 100258);
        map.put(FIM_MIDDLE, 100259);
        map.put(FIM_SUFFIX, 100260);
        map.put(ENDOFPROMPT, 100276);
        SPECIAL_TOKENS_CL100K_BASE = Collections.unmodifiableMap(map);
    }

    private EncodingFactory() {
    }

    /**
     * Returns an {@link Encoding} instance for the r50k_base encoding.
     *
     * @return an {@link Encoding} instance for the r50k_base encoding
     */
    public static Encoding r50kBase() {
        return fromPredefinedParameters(
                "r50k_base",
                "'s|'t|'re|'ve|'m|'ll|'d| ?\\p{L}+| ?\\p{N}+| ?[^\\s\\p{L}\\p{N}]+|\\s+(?!\\S)|\\s+",
                "/com/knuddels/jtokkit/r50k_base.tiktoken",
                SPECIAL_TOKENS_X50K_BASE
        );
    }

    /**
     * Returns an {@link Encoding} instance for the p50k_base encoding.
     *
     * @return an {@link Encoding} instance for the p50k_base encoding
     */
    public static Encoding p50kBase() {
        return fromPredefinedParameters(
                "p50k_base",
                "'s|'t|'re|'ve|'m|'ll|'d| ?\\p{L}+| ?\\p{N}+| ?[^\\s\\p{L}\\p{N}]+|\\s+(?!\\S)|\\s+",
                "/com/knuddels/jtokkit/p50k_base.tiktoken",
                SPECIAL_TOKENS_X50K_BASE
        );
    }

    /**
     * Returns an {@link Encoding} instance for the p50k_edit encoding.
     *
     * @return an {@link Encoding} instance for the p50k_edit encoding
     */
    public static Encoding p50kEdit() {
        return fromPredefinedParameters(
                "p50k_edit",
                "'s|'t|'re|'ve|'m|'ll|'d| ?\\p{L}+| ?\\p{N}+| ?[^\\s\\p{L}\\p{N}]+|\\s+(?!\\S)|\\s+",
                "/com/knuddels/jtokkit/p50k_base.tiktoken",
                SPECIAL_TOKENS_P50K_EDIT
        );
    }

    /**
     * Returns an {@link Encoding} instance for the cl100k_base encoding.
     *
     * @return an {@link Encoding} instance for the cl100k_base encoding
     */
    public static Encoding cl100kBase() {
        return fromPredefinedParameters(
                "cl100k_base",
                "(?i:'s|'t|'re|'ve|'m|'ll|'d)|[^\\r\\n\\p{L}\\p{N}]?\\p{L}+|\\p{N}{1,3}| ?[^\\s\\p{L}\\p{N}]+[\\r\\n]*|\\s*[\\r\\n]+|\\s+(?!\\S)|\\s+",
                "/com/knuddels/jtokkit/cl100k_base.tiktoken",
                SPECIAL_TOKENS_CL100K_BASE
        );
    }

    /**
     * Returns an {@link Encoding} instance for the given GPT BytePairEncoding parameters.
     *
     * @param parameters the GPT BytePairEncoding parameters
     * @return an {@link Encoding} instance for the given GPT BytePairEncoding parameters
     */
    public static Encoding fromParameters(GptBytePairEncodingParams parameters) {
        return new GptBytePairEncoding(parameters);
    }

    private static Encoding fromPredefinedParameters(
            String name,
            String patternString,
            String fileName,
            Map<String, Integer> specialTokens
    ) {
        Pattern regex;
        try {
            regex = Pattern.compile(patternString, Pattern.UNICODE_CHARACTER_CLASS);
        } catch (IllegalArgumentException exception) {
            // Workaround for Android where an IllegalArgumentException is thrown when using UNICODE_CHARACTER_CLASS
            regex = Pattern.compile(patternString);
        }

        GptBytePairEncodingParams params = new GptBytePairEncodingParams(name, regex, loadMergeableRanks(fileName), specialTokens);
        return fromParameters(params);
    }

    public static Map<byte[], Integer> loadMergeableRanks(String fileName) {
        try (InputStream in = EncodingFactory.class.getResourceAsStream(fileName)) {
            if (in == null) {
                throw new IllegalStateException("Could not find " + fileName + " in resources");
            }

            Map<byte[], Integer> mergeableRanks = new HashMap<>();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\s+", 2);
                if (parts.length != 2) {
                    throw new IllegalStateException("Invalid line in " + fileName + ": " + line);
                }

                byte[] token = Base64.getDecoder().decode(parts[0].getBytes(StandardCharsets.UTF_8));
                int rank = Integer.parseInt(parts[1]);

                mergeableRanks.put(token, rank);
            }

            return mergeableRanks;
        } catch (IOException e) {
            throw new IllegalStateException("Could not load " + fileName + " from resources", e);
        }
    }
}
