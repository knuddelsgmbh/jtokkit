package com.knuddels.jtokkit.api;

/**
 * The result of encoding operation.
 */
public final class EncodingResult {
    private final IntArrayList tokens;
    private final boolean truncated;
    private final int lastProcessedCharacterIndex;

    public EncodingResult(final IntArrayList tokens, final boolean truncated) {
        this(tokens, truncated, -1);
    }

    public EncodingResult(final IntArrayList tokens, final boolean truncated, final int lastProcessedCharacterIndex) {
        this.tokens = tokens;
        this.truncated = truncated;
        this.lastProcessedCharacterIndex = lastProcessedCharacterIndex;
    }

    /**
     * Returns the list of token ids
     *
     * @return the list of token ids
     */
    public IntArrayList getTokens() {
        return tokens;
    }

    /**
     * Returns true if the token list was truncated because the maximum token length was exceeded
     *
     * @return true if the token list was truncated because the maximum token length was exceeded
     */
    public boolean isTruncated() {
        return truncated;
    }

    /**
     * Returns the index of the last processed character in the input string
     *
     * @return the index of the last processed character in the input string, is -1 if text was null or empty
     */
    public int getLastProcessedCharacterIndex() {
        return lastProcessedCharacterIndex;
    }



    @Override
    public String toString() {
        return "EncodingResult{"
                + "tokens=" + tokens
                + ", truncated=" + truncated
                + ", lastProcessedCharacterIndex=" + lastProcessedCharacterIndex
                + '}';
    }
}