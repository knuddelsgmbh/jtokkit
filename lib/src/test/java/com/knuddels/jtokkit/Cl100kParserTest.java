package com.knuddels.jtokkit;

import static com.knuddels.jtokkit.Cl100kParser.addUtf8Bytes;
import static com.knuddels.jtokkit.Cl100kParser.isValidUTF8;
import static com.knuddels.jtokkit.EncodingFactory.compileRegex;
import static java.lang.Character.MAX_CODE_POINT;
import static java.lang.Character.MIN_CODE_POINT;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class Cl100kParserTest {
    public static Map<Integer, String> fetchUnicodeData() {
        var url = "https://www.unicode.org/Public/UCD/latest/ucd/UnicodeData.txt";
        Map<Integer, String> unicodeMap = new HashMap<>();

        try (var br = new BufferedReader(new InputStreamReader(new URI(url).toURL().openStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                var parts = line.split(";");
                assert parts.length > 1;
                var codePoint = Integer.parseInt(parts[0], 16);
                var name = parts[1];
                unicodeMap.put(codePoint, name);
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        return unicodeMap;
    }

    @Disabled // Takes too long
    @Test
    void testToUtf8BytesOnFetchedUnicodeData() {
        fetchUnicodeData().entrySet().stream().parallel().forEach(e -> {
            var expected = Character.toString(e.getKey());
            if (isValidUTF8(expected)) {
                var dst = new ByteArrayList();
                addUtf8Bytes(expected, 0, expected.length(), dst);

                assertArrayEquals(
                        expected.getBytes(UTF_8),
                        dst.toArray(),
                        () -> "Expected `" + Arrays.toString(expected.getBytes(UTF_8)) + "` (`" + expected + "`) but was `" + Arrays.toString(dst.toArray()) + "`"
                );
            } else {
                System.out.println("Skipping invalid UTF-8: " + e.getValue() + " (" + e.getKey() + ")");
            }
        });
    }

    @Test
    void testIsShortContraction() {
        var pattern = compileRegex("^(?:'s|'t|'re|'ve|'m|'ll|'d)$", true);

        for (var cp1 = MIN_CODE_POINT; cp1 <= MAX_CODE_POINT; cp1++) { // Seems 'ſ is also a contraction...
            var asString = "'" + Character.toString(cp1);
            var matchesRegex = pattern.matcher(asString).matches();
            var actual = Cl100kParser.isShortContraction(cp1);

            assertEquals(matchesRegex, actual, "Mismatch at code point: `" + asString + "` (" + cp1 + ")");
        }
    }

    @Disabled // takes too long
    @Test
    void testIsLongContraction() {
        var pattern = compileRegex("^(?:'s|'t|'re|'ve|'m|'ll|'d)$", true);

        for (var cp1 = MIN_CODE_POINT; cp1 <= MAX_CODE_POINT; cp1++) {
            for (var cp2 = MIN_CODE_POINT; cp2 <= MAX_CODE_POINT; cp2++) {
                var asString = "'" + Character.toString(cp1) + Character.toString(cp2);
                var matchesRegex = pattern.matcher(asString).matches();
                var actual = Cl100kParser.isLongContraction(cp1, cp2);

                assertEquals(matchesRegex, actual, "Mismatch at code point: `" + asString + "` (" + cp1 + ", " + cp2 + ")");
            }
        }
    }

    @Test
    void testIsNumeric() {
        var count = 0;
        assertFalse(Cl100kParser.isNumeric(-1));
        var pattern = compileRegex("^\\p{N}$", true);
        for (var cp = MIN_CODE_POINT; cp <= MAX_CODE_POINT; cp++) {
            var charAsString = Character.toString(cp);
            var matchesRegex = pattern.matcher(charAsString).matches();
            var actual = Cl100kParser.isNumeric(cp);
            if (matchesRegex) {
                count++;
            }

            assertEquals(matchesRegex, actual, "Mismatch at code point: `" + charAsString + "` (" + cp + ")");
        }
        System.out.println(count);
    }

    @Test
    void testIsLetter() {
        var count = 0;
        assertFalse(Cl100kParser.isLetter(-1));
        var pattern = compileRegex("^\\p{L}$", true);
        for (var cp = MIN_CODE_POINT; cp <= MAX_CODE_POINT; cp++) {
            var charAsString = Character.toString(cp);
            var matchesRegex = pattern.matcher(charAsString).matches();
            var actual = Cl100kParser.isLetter(cp);
            if (matchesRegex) {
                count++;
            }
            assertEquals(matchesRegex, actual, "Mismatch at code point: `" + charAsString + "` (" + cp + ")");
        }
        System.out.println(count);
    }

    @Test
    void testIsUnicodeWhitespace() {
        var count = 0;
        assertFalse(Cl100kParser.isWhitespace(-1));
        var pattern = compileRegex("^\\s$", true);
        for (var cp = MIN_CODE_POINT; cp <= MAX_CODE_POINT; cp++) {
            var charAsString = Character.toString(cp);
            var matchesRegex = pattern.matcher(charAsString).matches();
            var actual = Cl100kParser.isWhitespace(cp);
            if (matchesRegex) {
                count++;
            }
            assertEquals(matchesRegex, actual, "Mismatch at code point: `" + charAsString + "` (" + cp + ")");
        }
        System.out.println(count);
    }

    @Test
    void testIsLetterOrNumeric() {
        var count = 0;
        assertFalse(Cl100kParser.isLetterOrNumeric(-1));
        var pattern = compileRegex("^[\\p{L}\\p{N}]$", true);
        for (var cp = MIN_CODE_POINT; cp <= MAX_CODE_POINT; cp++) {
            var charAsString = Character.toString(cp);
            var matchesRegex = pattern.matcher(charAsString).matches();
            var actual = Cl100kParser.isLetterOrNumeric(cp);
            if (matchesRegex) {
                count++;
            }
            assertEquals(matchesRegex, actual, "Mismatch at code point: `" + charAsString + "` (" + cp + ")");
        }
        System.out.println(count);
    }

    @Test
    void testIsNotWhitespaceOrLetterOrNumeric() {
        var count = 0;
        assertFalse(Cl100kParser.isNotWhitespaceOrLetterOrNumeric(-1));
        var pattern = compileRegex("^[^\\s\\p{L}\\p{N}]$", true);
        for (var cp = MIN_CODE_POINT; cp <= MAX_CODE_POINT; cp++) {
            var charAsString = Character.toString(cp);
            var matchesRegex = pattern.matcher(charAsString).matches();
            var actual = Cl100kParser.isNotWhitespaceOrLetterOrNumeric(cp);
            if (matchesRegex) {
                count++;
            }
            assertEquals(matchesRegex, actual, "Mismatch at code point: `" + charAsString + "` (" + cp + ")");
        }
        System.out.println(count);
    }

    @Test
    void testIsNotNewlineOrLetterOrNumeric() {
        var count = 0;
        assertFalse(Cl100kParser.isNotNewlineOrLetterOrNumeric(-1));
        var pattern = compileRegex("^[^\r\n\\p{L}\\p{N}]$", true);
        for (var cp = MIN_CODE_POINT; cp <= MAX_CODE_POINT; cp++) {
            var charAsString = Character.toString(cp);
            var matchesRegex = pattern.matcher(charAsString).matches();
            var actual = Cl100kParser.isNotNewlineOrLetterOrNumeric(cp);
            if (matchesRegex) {
                count++;
            }
            assertEquals(matchesRegex, actual, "Mismatch at code point: `" + charAsString + "` (" + cp + ")");
        }
        System.out.println(count);
    }

    @Test
    void testIsNewline() {
        var count = 0;
        assertFalse(Cl100kParser.isNewline(-1));
        var pattern = compileRegex("^[\r\n]$", true);
        for (var cp = MIN_CODE_POINT; cp <= MAX_CODE_POINT; cp++) {
            var charAsString = Character.toString(cp);
            var matchesRegex = pattern.matcher(charAsString).matches();
            var isNewline = Cl100kParser.isNewline(cp);
            if (matchesRegex) {
                count++;
            }
            assertEquals(matchesRegex, isNewline, "Mismatch at code point: `" + charAsString + "` (" + cp + ")");
        }
        System.out.println(count);
    }
}