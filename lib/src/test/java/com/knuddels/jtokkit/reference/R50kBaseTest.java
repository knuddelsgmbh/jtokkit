package com.knuddels.jtokkit.reference;

import com.knuddels.jtokkit.Encodings;
import com.knuddels.jtokkit.api.Encoding;
import com.knuddels.jtokkit.api.EncodingResult;
import com.knuddels.jtokkit.api.EncodingType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class R50kBaseTest {

	private static final Encoding ENCODING = Encodings.newDefaultEncodingRegistry().getEncoding(EncodingType.R50K_BASE);

	@ParameterizedTest
	@CsvFileSource(resources = "/r50k_base_encodings.csv", numLinesToSkip = 1, maxCharsPerColumn = 1_000_000)
	public void r50kBaseEncodesCorrectly(
			final String input,
			final String output
	) {
		final List<Integer> expected = TestUtils.parseEncodingString(output);
		final List<Integer> actual = ENCODING.encode(input);

		assertEquals(expected, actual);
	}

	@ParameterizedTest
	@CsvFileSource(resources ="/r50k_base_encodings.csv", numLinesToSkip = 1, maxCharsPerColumn = 1_000_000)
	public void r50kBaseBaseEncodesStable(final String input) {
		final String actual = ENCODING.decode(ENCODING.encode(input));

		assertEquals(input, actual);
	}

	@ParameterizedTest
	@CsvFileSource(resources ="/r50k_base_encodings.csv", numLinesToSkip = 1, maxCharsPerColumn = 1_000_000)
	public void r50kBaseBaseEncodesCorrectlyWithMaxTokensSet(
			final String input,
			final String output,
			final String outputMaxTokens10
	) {
		final List<Integer> expected = TestUtils.parseEncodingString(output);
		final List<Integer> expectedWithMaxTokens = TestUtils.parseEncodingString(outputMaxTokens10);
		final EncodingResult encodingResult = ENCODING.encode(input, 10);

		assertEquals(expectedWithMaxTokens, encodingResult.getTokens());
		assertEquals(expected.size() > expectedWithMaxTokens.size(), encodingResult.isTruncated());
	}

	@ParameterizedTest
	@CsvFileSource(resources ="/r50k_base_encodings.csv", numLinesToSkip = 1, maxCharsPerColumn = 1_000_000)
	public void r50kBaseBaseEncodesStableWithMaxTokensSet(final String input) {
		final String actual = ENCODING.decode(ENCODING.encode(input, 10).getTokens());

		assertTrue(input.startsWith(actual));
	}

	@ParameterizedTest
	@CsvFileSource(resources ="/r50k_base_encodings.csv", numLinesToSkip = 1, maxCharsPerColumn = 1_000_000)
	public void r50kBaseBaseEncodeOrdinaryEncodesCorrectly(
			final String input,
			final String output
	) {
		final List<Integer> expected = TestUtils.parseEncodingString(output);
		final List<Integer> actual = ENCODING.encodeOrdinary(input);

		assertEquals(expected, actual);
	}

	@ParameterizedTest
	@CsvFileSource(resources ="/r50k_base_encodings.csv", numLinesToSkip = 1, maxCharsPerColumn = 1_000_000)
	public void r50kBaseBaseEncodeOrdinaryEncodesCorrectly(
			final String input,
			final String output,
			final String outputMaxTokens10
	) {
		final List<Integer> expected = TestUtils.parseEncodingString(output);
		final List<Integer> expectedWithMaxTokens = TestUtils.parseEncodingString(outputMaxTokens10);
		final EncodingResult encodingResult = ENCODING.encodeOrdinary(input, 10);

		assertEquals(expectedWithMaxTokens, encodingResult.getTokens());
		assertEquals(expected.size() > expectedWithMaxTokens.size(), encodingResult.isTruncated());
	}

	@ParameterizedTest
	@CsvFileSource(resources ="/r50k_base_encodings.csv", numLinesToSkip = 1, maxCharsPerColumn = 1_000_000)
	public void r50kBaseBaseEncodeOrdinaryEncodesStable(final String input) {
		final String actual = ENCODING.decode(ENCODING.encodeOrdinary(input));

		assertEquals(input, actual);
	}

	@ParameterizedTest
	@CsvFileSource(resources ="/r50k_base_encodings.csv", numLinesToSkip = 1, maxCharsPerColumn = 1_000_000)
	public void r50kBaseBaseEncodeOrdinaryEncodesStableWithMaxTokensSet(final String input) {
		final String actual = ENCODING.decode(ENCODING.encodeOrdinary(input, 10).getTokens());

		assertTrue(input.startsWith(actual));
	}

	@Test
	public void r50kBaseBaseEncodeOrdinaryEncodesSpecialTokensCorrectly() {
		final String input = "Hello<|endoftext|>, <|fim_prefix|> <|fim_middle|> world <|fim_suffix|> ! <|endofprompt|>";
		final String actual = ENCODING.decode(ENCODING.encodeOrdinary(input));

		assertEquals(input, actual);
	}
}
