package com.knuddels.jtokkit.api;

import java.util.List;

public interface Encoding {

	/**
	 * Encodes the given text into a list of token ids.
	 *
	 * @param text the text to encode
	 * @return the list of token ids
	 * @throws UnsupportedOperationException if the text contains special tokens which are not supported for now
	 */
	List<Integer> encode(String text);

	/**
	 * Encodes the given text into a list of token ids and returns the amount of tokens.
	 * This is a convenience method for {@link #encode(String)}, if all you want is to
	 * know the amount of tokens. It is not more performant than {@link #encode(String)},
	 * so prefer to use {@link #encode(String)} if you actually need the tokens.
	 *
	 * @param text the text to encode
	 * @return the amount of tokens
	 * @throws UnsupportedOperationException if the text contains special tokens which are not supported for now
	 */
	int countTokens(String text);

	/**
	 * Decodes the given list of token ids into a text.
	 *
	 * @param tokens the list of token ids
	 * @return the decoded text
	 * @throws IllegalArgumentException if the list contains invalid token ids
	 */
	String decode(List<Integer> tokens);

	/**
	 * Decodes the given list of token ids into a byte array.
	 *
	 * @param tokens the list of token ids
	 * @return the decoded byte array
	 * @throws IllegalArgumentException if the list contains invalid token ids
	 */
	byte[] decodeBytes(List<Integer> tokens);

	/**
	 * Returns the name of this encoding. This is the name which is used to identify
	 * the encoding and must be unique for registration in the {@link EncodingRegistry}.
	 *
	 * @return the name of this encoding
	 */
	String getName();
}
