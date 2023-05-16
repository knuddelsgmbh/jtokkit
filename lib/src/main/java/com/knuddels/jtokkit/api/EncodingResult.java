package com.knuddels.jtokkit.api;

import java.util.List;

/**
 * The result of encoding operation.
 */
public final class EncodingResult {
	private final List<Integer> tokens;
	private final boolean truncated;

	public EncodingResult(final List<Integer> tokens, final boolean truncated) {
		this.tokens = tokens;
		this.truncated = truncated;
	}

	/**
	 * @return the list of token ids
	 */
	public List<Integer> getTokens() {
		return tokens;
	}

	/**
	 * @return true if the token list was truncated because the maximum token length was exceeded
	 */
	public boolean isTruncated() {
		return truncated;
	}

	@Override
	public String toString() {
		return "EncodingResult{"
				+ "tokens=" + tokens
				+ ", truncated=" + truncated
				+ '}';
	}
}
