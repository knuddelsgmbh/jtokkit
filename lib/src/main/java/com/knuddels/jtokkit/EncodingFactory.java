package com.knuddels.jtokkit;

import static java.lang.Integer.parseInt;
import static java.nio.charset.StandardCharsets.UTF_8;

import com.knuddels.jtokkit.api.Encoding;
import com.knuddels.jtokkit.api.GptBytePairEncodingParams;
import com.knuddels.jtokkit.api.IntArrayList;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class EncodingFactory {
    private static final Map<String, Integer> SPECIAL_TOKENS_O200K_HARMONY;
    private static final Map<String, Integer> SPECIAL_TOKENS_O200K_BASE;
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

    static {
        Map<String, Integer> map = new HashMap<>();
        map.put(ENDOFTEXT, 199999);
        map.put(ENDOFPROMPT, 200018);
        SPECIAL_TOKENS_O200K_BASE = Collections.unmodifiableMap(map);
    }

    static {
        Map<String, Integer> map = new HashMap<>(SPECIAL_TOKENS_O200K_BASE);
        map.put("<|startoftext|>", 199998);
        map.put(ENDOFTEXT, 199999);
        map.put("<|reserved_200000|>", 200000);
        map.put("<|reserved_200001|>", 200001);
        map.put("<|return|>", 200002);
        map.put("<|constrain|>", 200003);
        map.put("<|reserved_200004|>", 200004);
        map.put("<|channel|>", 200005);
        map.put("<|start|>", 200006);
        map.put("<|end|>", 200007);
        map.put("<|message|>", 200008);
        map.put("<|reserved_200009|>", 200009);
        map.put("<|reserved_200010|>", 200010);
        map.put("<|reserved_200011|>", 200011);
        map.put("<|call|>", 200012);

        IntStream.range(200013, 201088)
                .forEach( i -> map.put("<|reserved_" + i + "|>", i));

        SPECIAL_TOKENS_O200K_HARMONY = Collections.unmodifiableMap(map);
    }

    private EncodingFactory() {
    }

    /**
     * Returns an {@link Encoding} instance for the r50k_base encoding.
     *
     * @return an {@link Encoding} instance for the r50k_base encoding
     */
    static Encoding r50kBase() {
        return from50kParameters(
                "r50k_base",
                "/com/knuddels/jtokkit/r50k_base.tiktoken",
                SPECIAL_TOKENS_X50K_BASE
        );
    }

    /**
     * Returns an {@link Encoding} instance for the p50k_base encoding.
     *
     * @return an {@link Encoding} instance for the p50k_base encoding
     */
    static Encoding p50kBase() {
        return from50kParameters(
                "p50k_base",
                "/com/knuddels/jtokkit/p50k_base.tiktoken",
                SPECIAL_TOKENS_X50K_BASE
        );
    }

    /**
     * Returns an {@link Encoding} instance for the p50k_edit encoding.
     *
     * @return an {@link Encoding} instance for the p50k_edit encoding
     */
    static Encoding p50kEdit() {
        return from50kParameters(
                "p50k_edit",
                "/com/knuddels/jtokkit/p50k_base.tiktoken",
                SPECIAL_TOKENS_P50K_EDIT
        );
    }

    /**
     * Returns an {@link Encoding} instance for the cl100k_base encoding.
     *
     * @return an {@link Encoding} instance for the cl100k_base encoding
     */
    static Encoding cl100kBase() {
        // "'(?:[sdmt]|ll|ve|re)|[^\r\n\\p{L}\\p{N}]?+\\p{L}+|\\p{N}{1,3}| ?[^\\s\\p{L}\\p{N}]++[\r\n]*|\\s*[\r\n]|\\s+(?!\\S)|\\s+"
        Map<byte[], Integer> mergeableRanks = loadMergeableRanks("/com/knuddels/jtokkit/cl100k_base.tiktoken");
        GptBytePairEncodingParams params = new GptBytePairEncodingParams("cl100k_base", null, mergeableRanks, SPECIAL_TOKENS_CL100K_BASE);
        return new Cl100kGptBytePairEncoding(params);
    }

    /**
     * Returns an {@link Encoding} instance for the o200k_base encoding.
     *
     * @return an {@link Encoding} instance for the o200k_base encoding
     */

    static Encoding o200kBase() {
        return from200kParameters("o200k_base", SPECIAL_TOKENS_O200K_BASE);
    }

    /**
     * Returns an {@link Encoding} instance for the o200k_harmony encoding.
     *
     * @return an {@link Encoding} instance for the o200k_harmony encoding
     */

    static Encoding o200kHarmony() {
        return from200kParameters("o200k_harmony", SPECIAL_TOKENS_O200K_HARMONY);
    }

    /**
     * Returns an {@link Encoding} instance for the given GPT BytePairEncoding parameters.
     *
     * @param parameters the GPT BytePairEncoding parameters
     * @return an {@link Encoding} instance for the given GPT BytePairEncoding parameters
     */
    static Encoding fromParameters(GptBytePairEncodingParams parameters) {
        return new GptBytePairEncoding(parameters);
    }

    private static Encoding from50kParameters(
            String name,
            String fileName,
            Map<String, Integer> specialTokens
    ) {
        Pattern regex = compileRegex("'(?:[sdmt]|ll|ve|re)| ?\\p{L}+| ?\\p{N}+| ?[^\\s\\p{L}\\p{N}]+|\\s+(?!\\S)|\\s+", false);
        Map<byte[], Integer> mergeableRanks = loadMergeableRanks(fileName);
        GptBytePairEncodingParams params = new GptBytePairEncodingParams(name, regex, mergeableRanks, specialTokens);
        return fromParameters(params);
    }

    private static Encoding from200kParameters(
            String name,
            Map<String, Integer> specialTokens
    ) {
        Map<byte[], Integer> mergeableRanks = loadMergeableRanks("/com/knuddels/jtokkit/o200k_base.tiktoken");
        List<String> patStrList = new ArrayList<>();
        patStrList.add("[^\\r\\n\\p{L}\\p{N}]?[\\p{Lu}\\p{Lt}\\p{Lm}\\p{Lo}\\p{M}]*[\\p{Ll}\\p{Lm}\\p{Lo}\\p{M}]+(?i:'s|'t|'re|'ve|'m|'ll|'d)?");
        patStrList.add("[^\\r\\n\\p{L}\\p{N}]?[\\p{Lu}\\p{Lt}\\p{Lm}\\p{Lo}\\p{M}]+[\\p{Ll}\\p{Lm}\\p{Lo}\\p{M}]*(?i:'s|'t|'re|'ve|'m|'ll|'d)?");
        patStrList.add("\\p{N}{1,3}");
        patStrList.add(" ?[^\\s\\p{L}\\p{N}]+[\\r\\n/]*");
        patStrList.add("\\s*[\\r\\n]+");
        patStrList.add("\\s+(?!\\S)");
        patStrList.add("\\s+");
        Pattern regex = compileRegex(patStrList.stream().map(String::valueOf).collect(Collectors.joining("|")), false);
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

    static Map<byte[], Integer> loadMergeableRanks(String fileName) {
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

    private static class Cl100kGptBytePairEncoding extends GptBytePairEncoding {
        Cl100kGptBytePairEncoding(GptBytePairEncodingParams params) {
            super(params);
        }

        @Override
        int encodeOrdinaryInternal(String text, int maxTokenCount, boolean keepEncodings, IntArrayList out) {
            int[] tokenCount = {0};
            IntArrayList ranks = new IntArrayList();
            Cl100kParser.split(text, utf8BytesList -> {
                tokenCount[0] += encoder.addTokensAndGetCount(maxTokenCount, keepEncodings, utf8BytesList.toArray(), out, ranks);
                return tokenCount[0] >= maxTokenCount;
            });
            return tokenCount[0];
        }
    }
}
