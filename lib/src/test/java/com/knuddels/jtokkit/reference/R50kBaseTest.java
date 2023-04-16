package com.knuddels.jtokkit.reference;

import com.knuddels.jtokkit.Encodings;
import com.knuddels.jtokkit.api.Encoding;
import com.knuddels.jtokkit.api.EncodingType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class R50kBaseTest {

	private static final Encoding ENCODING = Encodings.newDefaultEncodingRegistry().getEncoding(EncodingType.R50K_BASE);

	@ParameterizedTest
	@CsvFileSource(resources = "/r50k_base_encodings.csv", numLinesToSkip = 1, maxCharsPerColumn = 1_000_000)
	public void r50kBaseEncodesCorrectly(
			final String input,
			final String output,
			final String outputMaxTokens10
	) {
		//
		final List<Integer> expected = TestUtils.parseEncodingString(output);
		final List<Integer> actual = ENCODING.encode(input);
		assertEquals(expected, actual);

		// With MaxTokens set to 10
		final List<Integer> expectedWithMaxTokens = TestUtils.parseEncodingString(outputMaxTokens10);
		final List<Integer> actualWithMaxTokens = ENCODING.encode(input, 10);
		assertEquals(expectedWithMaxTokens, actualWithMaxTokens);
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/r50k_base_encodings.csv", numLinesToSkip = 1, maxCharsPerColumn = 1_000_000)
	public void r50kBaseEncodesStable(final String input) {
		final String actual = ENCODING.decode(ENCODING.encode(input));

		assertEquals(input, actual);
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/r50k_base_encodings.csv", numLinesToSkip = 1, maxCharsPerColumn = 1_000_000)
	public void r50kBaseEncodeOrdinaryEncodesCorrectly(
			final String input,
			final String output,
			final String outputMaxTokens10
	) {
		//
		final List<Integer> expected = TestUtils.parseEncodingString(output);
		final List<Integer> actual = ENCODING.encodeOrdinary(input);
		assertEquals(expected, actual);

		// With MaxTokens set to 10
		final List<Integer> expectedWithMaxTokens = TestUtils.parseEncodingString(outputMaxTokens10);
		final List<Integer> actualWithMaxTokens = ENCODING.encodeOrdinary(input, 10);
		assertEquals(expectedWithMaxTokens, actualWithMaxTokens);
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/r50k_base_encodings.csv", numLinesToSkip = 1, maxCharsPerColumn = 1_000_000)
	public void r50kBaseEncodeOrdinaryEncodesStable(final String input) {
		final String actual = ENCODING.decode(ENCODING.encodeOrdinary(input));

		assertEquals(input, actual);
	}

	@Test
	public void r50kBaseEncodeOrdinaryEncodesSpecialTokensCorrectly() {
		final String input = "Hello<|endoftext|>, world!";
		final String actual = ENCODING.decode(ENCODING.encodeOrdinary(input));

		assertEquals(input, actual);
	}
}
