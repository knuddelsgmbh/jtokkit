package com.knuddels.jtokkit.api;

/**
 * The result of encoding operation.
 */
public final class EncodingResult {
    private final IntArrayList tokens;
    private final boolean truncated;
    private final int lastTokenIndex;

    public EncodingResult(final IntArrayList tokens, final boolean truncated, int lastTokenIndex) {
        this.tokens = tokens;
        this.truncated = truncated;
        this.lastTokenIndex = lastTokenIndex;
    }

    public EncodingResult(final IntArrayList tokens, final boolean truncated) {
        this(tokens, truncated, -1);
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

    public int getLastTokenIndex() { return lastTokenIndex; }

    @Override
    public String toString() {
        return "EncodingResult{"
                + "tokens=" + tokens
                + ", truncated=" + truncated
                + '}';
    }

}