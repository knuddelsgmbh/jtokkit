package com.knuddels.jtokkit.api;

public interface Encoding {

    /**
     * Encodes the given text into a list of token ids.
     * <p>
     * Special tokens are artificial tokens used to unlock capabilities from a model,
     * such as fill-in-the-middle. There is currently no support for parsing special tokens
     * in a text, so if the text contains special tokens, this method will throw an
     * {@link UnsupportedOperationException}.
     * <p>
     * If you want to encode special tokens as ordinary text, use {@link #encodeOrdinary(String)}.
     * <pre>
     * Encoding encoding = EncodingRegistry.getEncoding(EncodingType.CL100K_BASE);
     * encoding.encode("hello world");
     * // returns [15339, 1917]
     *
     * encoding.encode("hello &lt;|endoftext|&gt; world");
     * // raises an UnsupportedOperationException
     * </pre>
     *
     * @param text the text to encode
     * @return the list of token ids
     * @throws UnsupportedOperationException if the text contains special tokens which are not supported for now
     */
    IntArrayList encode(String text);

    /**
     * Encodes the given text into a list of token ids.
     * <p>
     * Special tokens are artificial tokens used to unlock capabilities from a model,
     * such as fill-in-the-middle. There is currently no support for parsing special tokens
     * in a text, so if the text contains special tokens, this method will throw an
     * {@link UnsupportedOperationException}.
     * <p>
     * If you want to encode special tokens as ordinary text, use {@link #encodeOrdinary(String, int)}.
     * <p>
     * This method will truncate the list of token ids if the number of tokens exceeds the
     * given maxTokens parameter. Note that it will try to keep characters together, that are encoded into
     * multiple tokens. For example, if the text contains a character which is encoded into 3 tokens,
     * and due to the maxTokens parameter the last token of the character is truncated, the first two
     * tokens of the character will also be truncated. Therefore, the actual number of tokens may be
     * less than the given maxTokens parameter.
     * <pre>
     * Encoding encoding = EncodingRegistry.getEncoding(EncodingType.CL100K_BASE);
     * encoding.encode("hello world", 100);
     * // returns [15339, 1917]
     *
     * encoding.encode("hello &lt;|endoftext|&gt; world", 100);
     * // raises an UnsupportedOperationException
     * </pre>
     *
     * @param text      the text to encode
     * @param maxTokens the maximum number of tokens to encode
     * @return the {@link EncodingResult} containing a list of token ids and whether the tokens were truncated due to the maxTokens parameter
     * @throws UnsupportedOperationException if the text contains special tokens which are not supported for now
     */
    EncodingResult encode(String text, int maxTokens);

    /**
     * Encodes the given text into a list of token ids, ignoring special tokens.
     * <p>
     * This method does not throw an exception if the text contains special tokens, but instead
     * encodes them as if they were ordinary text.
     * <pre>
     * Encoding encoding = EncodingRegistry.getEncoding(EncodingType.CL100K_BASE);
     * encoding.encodeOrdinary("hello world");
     * // returns [15339, 1917]
     *
     * encoding.encodeOrdinary("hello &lt;|endoftext|&gt; world");
     * // returns [15339, 83739, 8862, 728, 428, 91, 29, 1917]
     * </pre>
     *
     * @param text the text to encode
     * @return the list of token ids
     */
    IntArrayList encodeOrdinary(String text);

    /**
     * Encodes the given text into a list of token ids, ignoring special tokens.
     * <p>
     * This method does not throw an exception if the text contains special tokens, but instead
     * encodes them as if they were ordinary text.
     * <p>
     * It will truncate the list of token ids if the number of tokens exceeds the
     * given maxTokens parameter. Note that it will try to keep characters together, that are encoded into
     * multiple tokens. For example, if the text contains a character which is encoded into 3 tokens,
     * and due to the maxTokens parameter the last token of the character is truncated, the first two
     * tokens of the character will also be truncated. Therefore, the actual number of tokens may be
     * less than the given maxTokens parameter.
     * <pre>
     * Encoding encoding = EncodingRegistry.getEncoding(EncodingType.CL100K_BASE);
     * encoding.encodeOrdinary("hello world", 100);
     * // returns [15339, 1917]
     *
     * encoding.encodeOrdinary("hello &lt;|endoftext|&gt; world", 100);
     * // returns [15339, 83739, 8862, 728, 428, 91, 29, 1917]
     * </pre>
     *
     * @param text      the text to encode
     * @param maxTokens the maximum number of tokens to encode
     * @return the {@link EncodingResult} containing a list of token ids and whether the tokens were truncated due to the maxTokens parameter
     */
    EncodingResult encodeOrdinary(String text, int maxTokens);

    /**
     * Encodes the given text into a list of token ids and returns the amount of tokens.
     * It is more performant than {@link #encode(String)}.
     * <pre>
     * Encoding encoding = EncodingRegistry.getEncoding(EncodingType.CL100K_BASE);
     * encoding.countTokens("hello world");
     * // returns 2
     *
     * encoding.countTokens("hello &lt;|endoftext|&gt; world");
     * // raises an UnsupportedOperationException
     * </pre>
     *
     * @param text the text to count tokens for
     * @return the amount of tokens
     * @throws UnsupportedOperationException if the text contains special tokens which are not supported for now
     */
    int countTokens(String text);

    /**
     * Decodes the given list of token ids into a text.
     * <pre>
     * Encoding encoding = EncodingRegistry.getEncoding(EncodingType.CL100K_BASE);
     * encoding.decode(List.of(15339, 1917));
     * // returns "hello world"
     *
     * encoding.decode(List.of(15339, 1917, Integer.MAX_VALUE));
     * // raises an IllegalArgumentException
     * </pre>
     *
     * @param tokens the list of token ids
     * @return the decoded text
     * @throws IllegalArgumentException if the list contains invalid token ids
     */
    String decode(IntArrayList tokens);

    /**
     * Decodes the given list of token ids into a byte array.
     * <pre>
     * Encoding encoding = EncodingRegistry.getEncoding(EncodingType.CL100K_BASE);
     * encoding.decodeBytes(List.of(15339, 1917));
     * // returns [104, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100]
     *
     * encoding.decodeBytes(List.of(15339, 1917, Integer.MAX_VALUE));
     * // raises an IllegalArgumentException
     * </pre>
     *
     * @param tokens the list of token ids
     * @return the decoded byte array
     * @throws IllegalArgumentException if the list contains invalid token ids
     */
    byte[] decodeBytes(IntArrayList tokens);

    /**
     * Returns the name of this encoding. This is the name which is used to identify
     * the encoding and must be unique for registration in the {@link EncodingRegistry}.
     *
     * @return the name of this encoding
     */
    String getName();
}
