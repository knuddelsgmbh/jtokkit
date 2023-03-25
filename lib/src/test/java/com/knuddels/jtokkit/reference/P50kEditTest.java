package com.knuddels.jtokkit.reference;

import com.knuddels.jtokkit.Encodings;
import com.knuddels.jtokkit.api.Encoding;
import com.knuddels.jtokkit.api.EncodingType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class P50kEditTest {

	private static final Encoding ENCODING = Encodings.newDefaultEncodingRegistry().getEncoding(EncodingType.P50K_EDIT);

	@ParameterizedTest
	@CsvFileSource(resources = "/p50k_edit_encodings.csv", numLinesToSkip = 1, maxCharsPerColumn = 1_000_000)
	public void cl100kBaseEncodesCorrectly(
			final String input,
			final String output
	) {
		final List<Integer> expected = TestUtils.parseEncodingString(output);
		final List<Integer> actual = ENCODING.encode(input);

		assertEquals(actual, expected);
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/p50k_edit_encodings.csv", numLinesToSkip = 1, maxCharsPerColumn = 1_000_000)
	public void cl100kBaseEncodesStable(final String input) {
		final String actual = ENCODING.decode(ENCODING.encode(input));

		assertEquals(actual, input);
	}
}
