package com.knuddels.jtokkit.reference;

import com.knuddels.jtokkit.Encodings;
import com.knuddels.jtokkit.api.Encoding;
import com.knuddels.jtokkit.api.EncodingType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class P50kBaseTest {

	private static final Encoding ENCODING = Encodings.newDefaultEncodingRegistry().getEncoding(EncodingType.P50K_BASE);

	@ParameterizedTest
	@CsvFileSource(resources = "/p50k_base_encodings.csv", numLinesToSkip = 1, maxCharsPerColumn = 1_000_000)
	public void p50kBaseEncodesCorrectly(
			final String input,
			final String output
	) {
		final List<Integer> expected = TestUtils.parseEncodingString(output);
		final List<Integer> actual = ENCODING.encode(input);

		assertEquals(actual, expected);
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/p50k_base_encodings.csv", numLinesToSkip = 1, maxCharsPerColumn = 1_000_000)
	public void p50kBaseEncodesStable(final String input) {
		final String actual = ENCODING.decode(ENCODING.encode(input));

		assertEquals(actual, input);
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/p50k_base_encodings.csv", numLinesToSkip = 1, maxCharsPerColumn = 1_000_000)
	public void p50kBaseEncodeOrdinaryEncodesCorrectly(
			final String input,
			final String output
	) {
		final List<Integer> expected = TestUtils.parseEncodingString(output);
		final List<Integer> actual = ENCODING.encodeOrdinary(input);

		assertEquals(actual, expected);
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/p50k_base_encodings.csv", numLinesToSkip = 1, maxCharsPerColumn = 1_000_000)
	public void p50kBaseEncodeOrdinaryEncodesStable(final String input) {
		final String actual = ENCODING.decode(ENCODING.encodeOrdinary(input));

		assertEquals(actual, input);
	}

	@Test
	public void p50kBaseEncodeOrdinaryEncodesSpecialTokensCorrectly() {
		final String input = "Hello<|endoftext|>, world!";
		final String actual = ENCODING.decode(ENCODING.encodeOrdinary(input));

		assertEquals(actual, input);
	}
}
