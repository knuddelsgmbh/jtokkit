package com.knuddels.jtokkit;

import com.knuddels.jtokkit.api.Encoding;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import static java.lang.Character.*;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.joining;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Cl100kBaseTest {
    private static final String PUNCTUATION = "'\".,?!:()";
    private static final String LETTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZő你好ſ ½";
    private static final String NUMBERS = "0123456789½";
    private static final String NEWLINES = "\n\r";
    private static final String WHITESPACES = " \t " + NEWLINES;
    private static final String NOT_NEWLINE_OR_LETTER_OR_NUMERIC = " \t🤚🏾😩" + PUNCTUATION;
    private static final String NOT_WHITESPACE_OR_LETTER_OR_NUMERIC = NOT_NEWLINE_OR_LETTER_OR_NUMERIC + NEWLINES;
    private static final List<String> SPECIAL = List.of("'s", "'t", "'re", "'ve", "'m", "'ll", "'d", "'ſ", "'x", "🤚🏾", "😩", "　", "½");

    private static final Encoding ENCODING = EncodingFactory.cl100kBase();

    private static String normalizeStringForTesting(String testString) {
        return testString
                .replaceAll("\\r", "\\\\r")
                .replaceAll("\\n", "\\\\n")
                .replaceAll(" ", "␣");
    }

    private static ThreadLocalRandom rand() {
        return ThreadLocalRandom.current();
    }

    @Disabled
    @Test
    void measureEncodingSpeeds() {
        var input = new StringBuilder();
        var measurements = new TreeMap<Integer, Long>();

        var iterations = 20;
        for (var i = 1.0; i < 3_000; i = Math.max(i + 1, i * 1.01)) {
            while (input.length() < i) {
                input.append("a");
            }
            var inputString = input.toString();

            for (var j = 0; j < 10 * iterations; j++) {
                var warmup = ENCODING.encode(inputString);
                assert !warmup.isEmpty();
            }
            var startTime = System.nanoTime();
            for (var j = 0; j < iterations; j++) {
                var encodingResult = ENCODING.encode(inputString);
                assert !encodingResult.isEmpty();
            }
            var endTime = System.nanoTime();
            measurements.put((int) i, ((endTime - startTime) / iterations));
        }
        measurements.forEach((i, t) -> System.out.println(i + "\t" + t));
    }

    @Test
    void testEdgeCaseRoundTrips() throws Exception {
        var testStrings = List.of(
                "\n",
                " ",
                "a : b",
                "  a",
                "\n \n ",
                "\n \n",
                "\n ",
                "\n \n!",
                "\n \n   ",
                "\n  !",
                "\n A",
                "  \n\r  \r\n  \r \n  A\nA \n A",
                ",\n\n",
                " ***\n\n\n\n",

                "   !",
                "   A",
                "   0",
                "   *",

                "   \n!",
                "   \nA",
                "   \n0",
                "   \n*",

                "   \n !",
                "   \n A",
                "   \n 0",
                "   \n *",

                "Many words map to one token, but some don't: indivisible.\n\nUnicode characters like emojis may be split into many tokens containing the underlying bytes: \uD83E\uDD1A\uD83C\uDFFE\n\nSequences of characters commonly found next to each other may be grouped together: 1234567890",
                "I paid $123,456 to 9876543210 people!",
                "Mixed script: 你好 world! 🌍",
                "Unicode snowman: ☃️",
                "I'm:  0\n",
                "We'll meet at 3 o'clock.",
                "Hello, world! It's a beautiful day...",
                "In 2023, I'll be 25 years old.",
                "Hello \n\n World  !",
                " It's 2:30pm;\n\n\n\nlet's eat, sleep , and code!",
                "'Thank God, here it is.' But when we took up the trunk...",
                "user@example.com",
                "this is a 'quoted' word",
                "　　a",
                "'ſ",
                "'ſ𣶸𣄬Ƙ淚",
                "😩\n",
                "03½",
                "* ע",
                "مرحبا بالعالم! كيف حالك؟ 😎",
                "\u0000\uD81C\uDFE1 a\u0000b-\u0000\u0000\u0000 \u0000",
                "🌍 a",
                "(𥧙h",
                ",   𰉄",
                "  󵨐)",
                "ﮀ\n ",
                "😐𪶫X",
                "෫𞅄",
                "𬕹\n  ",
                " 😈b\n\uD844\uDDAE'ſ\uD84F\uDDB8\uD84C\uDD2CƘ淚",
                "𗭾  󻥹\n\uD875\uDDB0蛇"
        );

        IntStream.range(0, testStrings.size()).forEachOrdered(i -> {
            var testString = testStrings.get(i);
            System.out.println("Validating `" + normalizeStringForTesting(testString) + "`");

            var actualTokens = ENCODING.encode(testString);
            var decoded = ENCODING.decode(actualTokens);
            assertEquals(testString, decoded, decoded);
        });
    }

    @Test
    void testRoundTripWithRandomStrings() throws Exception {
        var singleTokenStrings = getAllTokens();
        IntStream.range(0, 10_000).parallel().forEach(i -> {
            String testString;
            do {
                testString = generateRandomString(10, singleTokenStrings);
            } while (!UTF_8.newEncoder().canEncode(testString));

            var maxTokenCount = rand().nextInt(1, 2 * testString.length());

            var actualTokens = ENCODING.encode(testString);
            var decodedTokens = ENCODING.decode(actualTokens);
            assertEquals(testString, decodedTokens, decodedTokens);

            var actualTrimmedTokens = ENCODING.encode(testString, maxTokenCount).getTokens();
            var decodedTrimmedTokens = ENCODING.decode(actualTrimmedTokens);
            assertTrue(testString.startsWith(decodedTrimmedTokens));
        });
    }

    List<String> getAllTokens() {
        return EncodingFactory.loadMergeableRanks("/com/knuddels/jtokkit/cl100k_base.tiktoken").keySet().stream()
                .map(token -> new String(token, UTF_8))
                .toList();
    }

    String generateRandomString(int maxStringLength, List<String> singleTokenStrings) {
        var length = rand().nextInt(1, maxStringLength);
        return rand()
                .ints(length, 0, 20)
                .mapToObj(category -> getRandomCharFromCategory(category, singleTokenStrings))
                .map(String::valueOf)
                .map(obj -> rand().nextBoolean() ? obj : (rand().nextBoolean() ? obj.toUpperCase() : obj.toLowerCase()))
                .collect(joining());
    }

    char[] getRandomCharFromCategory(int category, List<String> singleTokenStrings) {
        switch (category) {
            case 0:
                return new char[]{' '};
            case 1:
                return new char[]{' ', ' '};
            case 2:
            case 3:
            case 4:
                return toChars((rand().nextBoolean() ? 'a' : 'A') + rand().nextInt('z' - 'a' + 1));
            case 5:
                return toChars(PUNCTUATION.codePointAt(rand().nextInt(PUNCTUATION.length())));
            case 6:
            case 7:
                return toChars(NEWLINES.codePointAt(rand().nextInt(NEWLINES.length())));
            case 8:
                return toChars(NUMBERS.codePointAt(rand().nextInt(NUMBERS.length())));
            case 9:
                return toChars(WHITESPACES.codePointAt(rand().nextInt(WHITESPACES.length())));
            case 10:
            case 11:
                return toChars(LETTERS.codePointAt(rand().nextInt(LETTERS.length())));
            case 12:
            case 13:
                return toChars(NOT_NEWLINE_OR_LETTER_OR_NUMERIC.codePointAt(rand().nextInt(NOT_NEWLINE_OR_LETTER_OR_NUMERIC.length())));
            case 14:
                return toChars(NOT_WHITESPACE_OR_LETTER_OR_NUMERIC.codePointAt(rand().nextInt(NOT_WHITESPACE_OR_LETTER_OR_NUMERIC.length())));
            case 15:
            case 16:
                return toChars(0x1F600 + rand().nextInt(0x50)); // emojis
            case 17:
                return SPECIAL.get(rand().nextInt(SPECIAL.size())).toCharArray();
            case 18:
                return singleTokenStrings.get(rand().nextInt(singleTokenStrings.size())).toCharArray();
            case 19:
                while (true) {
                    var r = rand().nextInt(MIN_CODE_POINT, MAX_CODE_POINT);
                    if (isDefined(r)) {
                        return toChars(r);
                    }
                }
            default:
                throw new IllegalStateException();
        }
    }
}
