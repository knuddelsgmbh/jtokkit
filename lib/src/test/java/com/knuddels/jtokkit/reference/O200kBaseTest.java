package com.knuddels.jtokkit.reference;

import com.knuddels.jtokkit.Encodings;
import com.knuddels.jtokkit.api.Encoding;
import com.knuddels.jtokkit.api.EncodingType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class O200kBaseTest {

    private static final Encoding ENCODING = Encodings.newDefaultEncodingRegistry().getEncoding(EncodingType.O200K_BASE);

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
    void o200kBaseBaseEncodesStable(String input) {
        var actual = ENCODING.decode(ENCODING.encode(input));

        assertEquals(input, actual);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/o200k_base_encodings.csv", numLinesToSkip = 1, maxCharsPerColumn = 1_000_000)
    void o200kBaseBaseEncodesCorrectlyWithMaxTokensSet(
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
    void o200kBaseBaseEncodesStableWithMaxTokensSet(String input) {
        var actual = ENCODING.decode(ENCODING.encode(input, 10).getTokens());

        assertTrue(input.startsWith(actual));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/o200k_base_encodings.csv", numLinesToSkip = 1, maxCharsPerColumn = 1_000_000)
    void o200kBaseBaseEncodeOrdinaryEncodesCorrectly(
            String input,
            String output
    ) {
        var expected = TestUtils.parseEncodingString(output);
        var actual = ENCODING.encodeOrdinary(input);

        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/o200k_base_encodings.csv", numLinesToSkip = 1, maxCharsPerColumn = 1_000_000)
    void o200kBaseBaseEncodeOrdinaryEncodesCorrectly(
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
    void o200kBaseBaseEncodeOrdinaryEncodesStable(String input) {
        var actual = ENCODING.decode(ENCODING.encodeOrdinary(input));

        assertEquals(input, actual);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/o200k_base_encodings.csv", numLinesToSkip = 1, maxCharsPerColumn = 1_000_000)
    void o200kBaseBaseEncodeOrdinaryEncodesStableWithMaxTokensSet(String input) {
        var actual = ENCODING.decode(ENCODING.encodeOrdinary(input, 10).getTokens());

        assertTrue(input.startsWith(actual));
    }

    @Test
    void o200kBaseBaseEncodeOrdinaryEncodesSpecialTokensCorrectly() {
        var input = "Hello<|endoftext|>, <|fim_prefix|> <|fim_middle|> world <|fim_suffix|> ! <|endofprompt|>";
        var actual = ENCODING.decode(ENCODING.encodeOrdinary(input));

        assertEquals(input, actual);
    }
}
