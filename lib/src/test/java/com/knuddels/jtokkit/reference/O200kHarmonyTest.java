package com.knuddels.jtokkit.reference;

import com.knuddels.jtokkit.Encodings;
import com.knuddels.jtokkit.api.Encoding;
import com.knuddels.jtokkit.api.EncodingType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class O200kHarmonyTest {

    private static final Encoding ENCODING = Encodings.newDefaultEncodingRegistry().getEncoding(EncodingType.O200K_HARMONY);

    @ParameterizedTest
    @CsvFileSource(resources = "/o200k_base_encodings.csv", numLinesToSkip = 1, maxCharsPerColumn = 1_000_000)
    void o200kBaseEncodesCorrectly(
            String input,
            String output
    ) {
        var expected = TestUtils.parseEncodingString(output);
        var actual = ENCODING.encode(input);

        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/o200k_base_encodings.csv", numLinesToSkip = 1, maxCharsPerColumn = 1_000_000)
    void o200kHarmonyEncodesStable(String input) {
        var actual = ENCODING.decode(ENCODING.encode(input));

        assertEquals(input, actual);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/o200k_base_encodings.csv", numLinesToSkip = 1, maxCharsPerColumn = 1_000_000)
    void o200kHarmonyEncodesCorrectlyWithMaxTokensSet(
            String input,
            String output,
            String outputMaxTokens10
    ) {
        var expected = TestUtils.parseEncodingString(output);
        var expectedWithMaxTokens = TestUtils.parseEncodingString(outputMaxTokens10);
        var encodingResult = ENCODING.encode(input, 10);

        assertEquals(expectedWithMaxTokens, encodingResult.getTokens());
        assertEquals(expected.size() > expectedWithMaxTokens.size(), encodingResult.isTruncated());
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/o200k_base_encodings.csv", numLinesToSkip = 1, maxCharsPerColumn = 1_000_000)
    void o200kHarmonyEncodesStableWithMaxTokensSet(String input) {
        var actual = ENCODING.decode(ENCODING.encode(input, 10).getTokens());

        assertTrue(input.startsWith(actual));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/o200k_base_encodings.csv", numLinesToSkip = 1, maxCharsPerColumn = 1_000_000)
    void o200kHarmonyEncodeOrdinaryEncodesCorrectly(
            String input,
            String output
    ) {
        var expected = TestUtils.parseEncodingString(output);
        var actual = ENCODING.encodeOrdinary(input);

        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/o200k_base_encodings.csv", numLinesToSkip = 1, maxCharsPerColumn = 1_000_000)
    void o200kHarmonyEncodeOrdinaryEncodesCorrectly(
            String input,
            String output,
            String outputMaxTokens10
    ) {
        var expected = TestUtils.parseEncodingString(output);
        var expectedWithMaxTokens = TestUtils.parseEncodingString(outputMaxTokens10);
        var encodingResult = ENCODING.encodeOrdinary(input, 10);

        assertEquals(expectedWithMaxTokens, encodingResult.getTokens());
        assertEquals(expected.size() > expectedWithMaxTokens.size(), encodingResult.isTruncated());
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/o200k_base_encodings.csv", numLinesToSkip = 1, maxCharsPerColumn = 1_000_000)
    void o200kHarmonyEncodeOrdinaryEncodesStable(String input) {
        var actual = ENCODING.decode(ENCODING.encodeOrdinary(input));

        assertEquals(input, actual);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/o200k_base_encodings.csv", numLinesToSkip = 1, maxCharsPerColumn = 1_000_000)
    void o200kHarmonyEncodeOrdinaryEncodesStableWithMaxTokensSet(String input) {
        var actual = ENCODING.decode(ENCODING.encodeOrdinary(input, 10).getTokens());

        assertTrue(input.startsWith(actual));
    }

    @Test
    void o200kHarmonyEncodeOrdinaryEncodesSpecialTokensCorrectly() {
        var input = "<|startoftext|>Hello<|endoftext|>, <|start|> <|end|> world <|reserved_201088|> ! <|endofprompt|>";
        var actual = ENCODING.decode(ENCODING.encodeOrdinary(input));

        assertEquals(input, actual);
    }
}
