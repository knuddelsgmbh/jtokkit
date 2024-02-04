package com.knuddels.jtokkit;


import java.util.Arrays;
import java.util.function.Predicate;

import static java.lang.Character.*;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Arrays.binarySearch;

public class Cl100kParser {
    private static final String SDTM = "sdtmSDTMſ";
    private static final String SIMPLE_WHITESPACES = "\t\n\u000B\u000C\r";
    private static final int[] REMAINING_WHITESPACES = "\u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200A\u2028\u2029\u202F\u205F\u3000".codePoints().sorted().toArray();

    public static void split(String input, Predicate<ByteArrayList> fragmentConsumer) {
        assert isValidUTF8(input) : "Input is not UTF-8: " + input;
        ByteArrayList utf8Bytes = new ByteArrayList();
        boolean finished = false;
        for (int endIndex = 0; endIndex < input.length() && !finished; ) {
            int startIndex = endIndex;
            int c0 = input.codePointAt(startIndex);
            int cc0 = charCount(c0);
            int nextIndex = startIndex + cc0;
            int c1 = (nextIndex < input.length()) ? input.codePointAt(nextIndex) : -1;

            if ((c0 == '\'') && c1 > 0) {
                if (isShortContraction(c1)) {
                    // 1) `'[sdtm]` - contractions, such as the suffixes of `he's`, `I'd`, `'tis`, `I'm`
                    endIndex += 2;
                    finished = fragmentConsumer.test(addUtf8Bytes(input, startIndex, endIndex, utf8Bytes));
                    continue;
                } else if ((startIndex + 2) < input.length() && isLongContraction(c1, input.codePointAt(startIndex + 2))) {
                    // 1) `'(?:ll|ve|re)` - contractions, such as the suffixes of `you'll`, `we've`, `they're`
                    endIndex += 3;
                    finished = fragmentConsumer.test(addUtf8Bytes(input, startIndex, endIndex, utf8Bytes));
                    continue;
                }
            }

            int cc1 = charCount(c1);
            if ((isNotNewlineOrLetterOrNumeric(c0) && isLetter(c1)) || isLetter(c0)) {
                // 2) `[^\r\n\p{L}\p{N}]?+\p{L}+` - words such as ` of`, `th`, `It`, ` not`
                endIndex += cc0;
                if (isLetter(c1)) {
                    endIndex += cc1;
                    while ((endIndex < input.length()) && isLetter(c0 = input.codePointAt(endIndex))) {
                        endIndex += charCount(c0);
                    }
                }
                finished = fragmentConsumer.test(addUtf8Bytes(input, startIndex, endIndex, utf8Bytes));
            } else if (isNumeric(c0)) {
                // 3) `\p{N}{1,3}` - numbers, such as `4`, `235` or `3½`
                endIndex += cc0;
                if (isNumeric(c1)) {
                    endIndex += cc1;
                    if ((endIndex < input.length()) && isNumeric(c0 = input.codePointAt(endIndex))) {
                        endIndex += charCount(c0);
                    }
                }
                finished = fragmentConsumer.test(addUtf8Bytes(input, startIndex, endIndex, utf8Bytes));
            } else if (isNotWhitespaceOrLetterOrNumeric(c0) || ((c0 == ' ') && isNotWhitespaceOrLetterOrNumeric(c1))) {
                // 4) ` ?[^\s\p{L}\p{N}]++[\r\n]*` - punctuation, such as `,`, ` .`, `"`
                endIndex += cc0;
                if ((endIndex < input.length()) && isNotWhitespaceOrLetterOrNumeric(c1)) {
                    endIndex += cc1;
                    while ((endIndex < input.length()) && isNotWhitespaceOrLetterOrNumeric(c0 = input.codePointAt(endIndex))) {
                        endIndex += charCount(c0);
                    }
                }
                while ((endIndex < input.length()) && isNewline(input.codePointAt(endIndex))) {
                    endIndex++;
                }
                finished = fragmentConsumer.test(addUtf8Bytes(input, startIndex, endIndex, utf8Bytes));
            } else {
                // 5) `\s*[\r\n]+` - line endings such as `\r\n    \r\n`
                // 6) `\s+(?!\S)` - whitespaces such as `               ` or ` `
                // 7) `\s+` - unmatched remaining spaces, such as ` `
                assert isWhitespace(c0) : "Invalid character: " + Arrays.toString(toChars(c0));
                int lastNewLineIndex = isNewline(c0) ? endIndex : -1;
                endIndex += cc0;
                if (isWhitespace(c1)) {
                    lastNewLineIndex = isNewline(c1) ? endIndex : lastNewLineIndex;
                    endIndex += cc1;
                    while (endIndex < input.length() && isWhitespace(c0 = input.codePointAt(endIndex))) {
                        lastNewLineIndex = isNewline(c0) ? endIndex : lastNewLineIndex;
                        endIndex += charCount(c0);
                    }
                }

                if (lastNewLineIndex > -1) {
                    int finalEndIndex = endIndex;
                    endIndex = lastNewLineIndex + 1;
                    if (endIndex < finalEndIndex) {
                        assert startIndex < endIndex;
                        finished = fragmentConsumer.test(addUtf8Bytes(input, startIndex, endIndex, utf8Bytes));
                        startIndex = endIndex;
                        endIndex = finalEndIndex;
                    }
                }
                if (!finished) {
                    if (lastNewLineIndex + 1 < endIndex && !isWhitespace(c0)) {
                        endIndex--;
                    }
                    if (startIndex < endIndex) {
                        finished = fragmentConsumer.test(addUtf8Bytes(input, startIndex, endIndex, utf8Bytes));
                    }
                }
            }
        }
    }


    static boolean isShortContraction(int ch) {
        return SDTM.indexOf(ch) >= 0;
    }

    static boolean isLongContraction(int ch1, int ch2) {
        if (((ch1 == 'l') && (ch2 == 'l'))
                || ((ch1 == 'v') && (ch2 == 'e'))
                || ((ch1 == 'r') && (ch2 == 'e'))) {
            return true;
        } else {
            int lch1 = toUpperCase(ch1);
            int lch2 = toUpperCase(ch2);
            return ((lch1 == 'L') && (lch2 == 'L'))
                    || ((lch1 == 'V') && (lch2 == 'E'))
                    || ((lch1 == 'R') && (lch2 == 'E'));
        }
    }

    public static boolean isValidUTF8(String input) {
        return UTF_8.newEncoder().canEncode(input);
    }

    public static boolean isLetter(int ch) {
        if (ch < 0xaa) {
            return ((ch >= 'a') && (ch <= 'z'))
                    || ((ch >= 'A') && (ch <= 'Z'));
        } else if (ch <= 0x323af) {
            switch (getType(ch)) {
                case UPPERCASE_LETTER:
                case LOWERCASE_LETTER:
                case TITLECASE_LETTER:
                case MODIFIER_LETTER:
                case OTHER_LETTER:
                    return true;
            }
        }
        return false;
    }

    public static boolean isNumeric(int ch) {
        if (ch < 0xb2) {
            return (ch >= '0') && (ch <= '9');
        } else if (ch <= 0x1fbf9) {
            switch (getType(ch)) {
                case DECIMAL_DIGIT_NUMBER:
                case LETTER_NUMBER:
                case OTHER_NUMBER:
                    return true;
            }
        }
        return false;
    }

    static boolean isLetterOrNumeric(int ch) {
        if (ch < 0xaa) {
            return ((ch >= 'a') && (ch <= 'z'))
                    || ((ch >= 'A') && (ch <= 'Z'))
                    || ((ch >= '0') && (ch <= '9'));
        } else if (ch <= 0x323af) {
            switch (getType(ch)) {
                case UPPERCASE_LETTER:
                case LOWERCASE_LETTER:
                case TITLECASE_LETTER:
                case MODIFIER_LETTER:
                case OTHER_LETTER:
                case DECIMAL_DIGIT_NUMBER:
                case LETTER_NUMBER:
                case OTHER_NUMBER:
                    return true;
            }
        }
        return false;
    }

    public static boolean isWhitespace(int ch) {
        if (ch <= '\r') {
            return SIMPLE_WHITESPACES.indexOf(ch) >= 0;
        } else if (ch < '\u0085') {
            return ch == ' ';
        } else {
            return (ch == '\u0085')
                    || (ch == '\u00A0')
                    || ((ch >= '\u1680') && (ch <= '\u3000') && (binarySearch(REMAINING_WHITESPACES, ch) >= 0));
        }
    }

    static boolean isNewline(int ch) {
        return (ch == '\r')
                || (ch == '\n');
    }

    public static boolean isNotWhitespaceOrLetterOrNumeric(int ch) {
        if (ch < '0') {
            return ch >= 0 && ch != ' ' && (ch > '\r' || ch < '\t');
        } else {
            return !isLetterOrNumeric(ch) && !isWhitespace(ch);
        }
    }

    public static boolean isNotNewlineOrLetterOrNumeric(int ch) {
        if (ch < '0') {
            return ch >= 0 && (ch == ' ' || !isNewline(ch));
        } else {
            return !isLetterOrNumeric(ch);
        }
    }

    static ByteArrayList addUtf8Bytes(String input, int start, int end, ByteArrayList dst) {
        dst.clear();
        for (int i = start; i < end; i++) {
            int cp = input.codePointAt(i);
            if (cp < 0x80) {
                dst.add((byte) cp);
            } else if (cp < 0x800) {
                dst.add((byte) (0xc0 | (cp >> 0x6)));
                dst.add((byte) (0x80 | (cp & 0x3f)));
            } else if (cp < MIN_SUPPLEMENTARY_CODE_POINT) {
                dst.add((byte) (0xe0 | (cp >> 0xc)));
                dst.add((byte) (0x80 | ((cp >> 0x6) & 0x3f)));
                dst.add((byte) (0x80 | (cp & 0x3f)));
            } else {
                assert cp < (MAX_CODE_POINT + 1) : "Invalid code point: " + cp;
                dst.add((byte) (0xf0 | (cp >> 0x12)));
                dst.add((byte) (0x80 | ((cp >> 0xc) & 0x3f)));
                dst.add((byte) (0x80 | ((cp >> 0x6) & 0x3f)));
                dst.add((byte) (0x80 | (cp & 0x3f)));
                i++;
            }
        }
        return dst;
    }
}