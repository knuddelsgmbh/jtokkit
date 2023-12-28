package com.knuddels.jtokkit;

import com.knuddels.jtokkit.api.Encoding;
import com.knuddels.jtokkit.api.GptBytePairEncodingParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;
import static java.nio.charset.StandardCharsets.UTF_8;

class EncodingFactory {
    private static final Map<String, Integer> SPECIAL_TOKENS_CL100K_BASE;
    private static final Map<String, Integer> SPECIAL_TOKENS_X50K_BASE;
    private static final Map<String, Integer> SPECIAL_TOKENS_P50K_EDIT;

    private static final String ENDOFTEXT = "<|endoftext|>";
    private static final String FIM_PREFIX = "<|fim_prefix|>";
    private static final String FIM_MIDDLE = "<|fim_middle|>";
    private static final String FIM_SUFFIX = "<|fim_suffix|>";
    private static final String ENDOFPROMPT = "<|endofprompt|>";

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
                "'(?:[sdmt]|ll|ve|re)| ?\\p{L}+| ?\\p{N}+| ?[^\\s\\p{L}\\p{N}]+|\\s+(?!\\S)|\\s+",
                "/com/knuddels/jtokkit/r50k_base.tiktoken",
                SPECIAL_TOKENS_X50K_BASE,
                false
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
                "'(?:[sdmt]|ll|ve|re)| ?\\p{L}+| ?\\p{N}+| ?[^\\s\\p{L}\\p{N}]+|\\s+(?!\\S)|\\s+",
                "/com/knuddels/jtokkit/p50k_base.tiktoken",
                SPECIAL_TOKENS_X50K_BASE,
                false
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
                "'(?:[sdmt]|ll|ve|re)| ?\\p{L}+| ?\\p{N}+| ?[^\\s\\p{L}\\p{N}]+|\\s+(?!\\S)|\\s+",
                "/com/knuddels/jtokkit/p50k_base.tiktoken",
                SPECIAL_TOKENS_P50K_EDIT,
                false
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
                "'(?:[sdmt]|ll|ve|re)|[^\r\n\\p{L}\\p{N}]?+\\p{L}+|\\p{N}{1,3}| ?[^\\s\\p{L}\\p{N}]++[\r\n]*|\\s*[\r\n]|\\s+(?!\\S)|\\s+",
                "/com/knuddels/jtokkit/cl100k_base.tiktoken",
                SPECIAL_TOKENS_CL100K_BASE,
                true
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
            Map<String, Integer> specialTokens,
            boolean caseInsensitive
    ) {
        Pattern regex = compileRegex(patternString, caseInsensitive);
        Map<byte[], Integer> mergeableRanks = loadMergeableRanks(fileName);
        GptBytePairEncodingParams params = new GptBytePairEncodingParams(name, regex, mergeableRanks, specialTokens);
        return fromParameters(params);
    }

    static Pattern compileRegex(String patternString, boolean caseInsensitive) {
        try {
            int flags = Pattern.UNICODE_CHARACTER_CLASS;
            if (caseInsensitive) {
                flags |= Pattern.CASE_INSENSITIVE;
            }
            return Pattern.compile(patternString, flags);
        } catch (IllegalArgumentException exception) {
            // Workaround for Android where an IllegalArgumentException is thrown when using UNICODE_CHARACTER_CLASS
            int flags = 0;
            if (caseInsensitive) {
                flags = Pattern.CASE_INSENSITIVE;
            }
            return Pattern.compile(patternString, flags);
        }
    }

    public static Map<byte[], Integer> loadMergeableRanks(String fileName) {
        try (InputStream in = EncodingFactory.class.getResourceAsStream(fileName)) {
            if (in == null) {
                throw new IllegalStateException("Could not find " + fileName + " in resources");
            }

            Map<byte[], Integer> mergeableRanks = new LinkedHashMap<>(); // keep order to optimize collisions
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                int firstSpaceIndex = line.indexOf(' ');
                assert firstSpaceIndex != -1 : "Invalid line in " + fileName + ": " + line;

                byte[] token = Base64.getDecoder().decode(line.substring(0, firstSpaceIndex).getBytes(UTF_8));
                int rank = parseInt(line.substring(firstSpaceIndex + 1));

                mergeableRanks.put(token, rank);
            }

            return mergeableRanks;
        } catch (IOException e) {
            throw new IllegalStateException("Could not load " + fileName + " from resources", e);
        }
    }
}
