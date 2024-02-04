package com.knuddels.jtokkit;

import static com.knuddels.jtokkit.TokenEncoderLarge.calculateTokensLarge;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.parseInt;
import static java.util.Collections.emptyMap;

import com.knuddels.jtokkit.api.IntArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public final class TokenEncoder {
    public static final int MAX_RANK = MAX_VALUE - 1;
    public static final String VERY_LARGE_TOKENIZER_BYTE_THRESHOLD_KEY = "VERY_LARGE_TOKENIZER_BYTE_THRESHOLD";
    static final int DUMMY_RANK = MAX_VALUE;
    private final Map<ByteArrayWrapper, Integer>[] encoders;
    private final Map<Integer, byte[]> decoder;

    private int VERY_LARGE_TOKENIZER_BYTE_THRESHOLD;

    TokenEncoder(Map<byte[], Integer> encoder) {
        if (!encoder.isEmpty()) {
            VERY_LARGE_TOKENIZER_BYTE_THRESHOLD = parseInt(System.getProperty(VERY_LARGE_TOKENIZER_BYTE_THRESHOLD_KEY, "500"));
            TreeMap<Integer, Map<ByteArrayWrapper, Integer>> tempEncoders = new TreeMap<>();
            encoder.forEach((k, v) -> {
                ByteArrayWrapper key = new ByteArrayWrapper(k);
                tempEncoders.computeIfAbsent(k.length, integer -> new HashMap<>()).put(key, v);
            });
            //noinspection unchecked
            encoders = new Map[tempEncoders.lastKey() + 1];
            tempEncoders.forEach((k, v) -> encoders[k] = v);

            this.decoder = new HashMap<>(encoder.size());
            encoder.forEach((k, v) -> decoder.put(v, k));
        } else {
            //noinspection unchecked
            encoders = new Map[0]; // for testing
            decoder = emptyMap();
        }
    }

    private static int getMinRankIndex(IntArrayList ranks) {
        int minRankIndex = -1;
        int minRank = MAX_RANK;

        int i = 0;
        int length = ranks.size() - 3;
        for (; i < length - 2; i += 4) { // Unrolled loop
            {
                int r = ranks.get(i);
                if (r < minRank) {
                    minRankIndex = i;
                    minRank = r;
                }
            }
            {
                int r = ranks.get(i + 1);
                if (r < minRank) {
                    minRankIndex = i + 1;
                    minRank = r;
                }
            }
            {
                int r = ranks.get(i + 2);
                if (r < minRank) {
                    minRankIndex = i + 2;
                    minRank = r;
                }
            }
            {
                int r = ranks.get(i + 3);
                if (r < minRank) {
                    minRankIndex = i + 3;
                    minRank = r;
                }
            }
        }

        for (; i <= length; i++) {
            int r = ranks.get(i);
            if (r < minRank) {
                minRankIndex = i;
                minRank = r;
            }
        }

        return minRankIndex;
    }

    private static int getNextIndex(IntArrayList ranks, int nextIndex) {
        while (nextIndex < ranks.size() && ranks.get(nextIndex) == DUMMY_RANK) {
            nextIndex++;
        }
        return nextIndex;
    }

    private static int getPreviousIndex(IntArrayList ranks, int previousIndex) {
        while (previousIndex >= 0 && ranks.get(previousIndex) == DUMMY_RANK) {
            previousIndex--;
        }
        return previousIndex;
    }

    int addTokensAndGetCount(int maxTokenCount, boolean keepEncodings, byte[] byteArray, IntArrayList out, IntArrayList ranks) {
        ByteArrayWrapper match = new ByteArrayWrapper(byteArray);
        int encoded = encode(match);
        if (encoded != MAX_RANK) {
            if (keepEncodings) {
                out.add(encoded);
            }
            return 1;
        } else {
            if (match.length() < VERY_LARGE_TOKENIZER_BYTE_THRESHOLD) {
                return calculateTokensSmall(maxTokenCount, keepEncodings, out, ranks, match);
            } else {
                return calculateTokensLarge(this, maxTokenCount, keepEncodings, out, match);
            }
        }
    }

    private int calculateTokensSmall(int maxTokenCount, boolean keepEncodings, IntArrayList out, IntArrayList ranks, ByteArrayWrapper match) {
        int length = match.length();
        assert length > 1 : "Already filtered out";
        ranks.clear();
        ranks.ensureCapacity(length + 1);

        int minRankIndex = -1;
        for (int i = 0, minRank = MAX_RANK; i < length + 1; i++) {
            int encoded = encode(match, i, i + 2);
            if (encoded != MAX_RANK) {
                if (encoded < minRank) {
                    minRankIndex = i;
                    minRank = encoded;
                }
            }
            ranks.add(encoded);
        }
        int tokenCount = mergeBytesAndGetTokenCount(match, length, ranks, minRankIndex);
        if (keepEncodings) {
            for (int start = 0, end = 1; end < ranks.size() && out.size() < maxTokenCount; end++) {
                if (ranks.get(end) != DUMMY_RANK) {
                    int token = encode(match, start, end);
                    assert token != MAX_RANK : "Token should not be MAX_RANK";
                    out.add(token);
                    start = end;
                }
            }
        }
        return tokenCount;
    }

    int mergeBytesAndGetTokenCount(ByteArrayWrapper piece, int length, IntArrayList ranks, int minRankIndex) {
        assert getMinRankIndex(ranks) == minRankIndex;
        while (minRankIndex >= 0) {
            int previousIndex = getPreviousIndex(ranks, minRankIndex - 1);
            int nextIndex = getNextIndex(ranks, minRankIndex + 1);
            int nextNextIndex = getNextIndex(ranks, nextIndex + 1);
            int nextNextNextIndex = getNextIndex(ranks, nextNextIndex + 1);

            if (previousIndex >= 0) {
                assert ranks.get(previousIndex) != DUMMY_RANK;
                int newRank = encode(piece, previousIndex, nextNextIndex);
                ranks.set(previousIndex, newRank);
            }
            assert ranks.get(minRankIndex) != DUMMY_RANK;
            int newRank = encode(piece, minRankIndex, nextNextNextIndex);
            ranks.set(minRankIndex, newRank);

            ranks.set(nextIndex, DUMMY_RANK);

            length--;
            if (length < 3) {
                break; // single tokens were already filtered out, let's skip a minimum calculation
            } else {
                minRankIndex = getMinRankIndex(ranks);
            }
        }
        assert getMinRankIndex(ranks) < 0;
        return length;
    }

    private int encode(ByteArrayWrapper payload) {
        if (payload.length() < encoders.length) {
            Map<ByteArrayWrapper, Integer> encoder = encoders[payload.length()];
            if (encoder != null) {
                Integer result = encoder.get(payload);
                if (result != null) {
                    return result;
                }
            }
        }
        return MAX_RANK;
    }

    int encode(ByteArrayWrapper piece, int start, int end) {
        if (end > piece.length() || end - start == piece.length()) {
            return MAX_RANK;
        } else {
            return encode(piece.getBytesBetween(start, end));
        }
    }

    byte[] decodeToken(int token, SpecialEncoder specialEncoder) {
        return decoder.computeIfAbsent(token, specialEncoder::decodeIfPresent);
    }
}