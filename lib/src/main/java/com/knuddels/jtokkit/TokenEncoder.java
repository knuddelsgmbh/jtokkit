package com.knuddels.jtokkit;

import java.util.*;

import static com.knuddels.jtokkit.TokenEncoderLarge.calculateTokensLarge;

final class TokenEncoder {
    public static final int DUMMY_RANK = Integer.MAX_VALUE;
    public static final int MAX_RANK = Integer.MAX_VALUE - 1;
    private final Map<ByteArrayWrapper, Integer>[] encoders;
    private int VERY_LARGE_TOKENIZER_BYTE_THRESHOLD;
    private int length = 0;

    public TokenEncoder(Map<byte[], Integer> encoder) {
        if (!encoder.isEmpty()) {
            VERY_LARGE_TOKENIZER_BYTE_THRESHOLD = Integer.parseInt(System.getProperty("VERY_LARGE_TOKENIZER_BYTE_THRESHOLD", "500"));
            TreeMap<Integer, Map<ByteArrayWrapper, Integer>> tempEncoders = new TreeMap<>();
            encoder.forEach((k, v) -> {
                length++;
                ByteArrayWrapper key = new ByteArrayWrapper(k);
                tempEncoders.computeIfAbsent(k.length, integer -> new HashMap<>()).put(key, v);
            });
            //noinspection unchecked
            encoders = new Map[tempEncoders.lastKey() + 1];
            tempEncoders.forEach((k, v) -> encoders[k] = v);
        } else {
            //noinspection unchecked
            encoders = new Map[0]; // for testing
        }
    }

    private static int getMinRankIndex(List<Integer> ranks) {
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

    private static int getNextIndex(List<Integer> ranks, int nextIndex) {
        while (nextIndex < ranks.size() && ranks.get(nextIndex) == DUMMY_RANK) {
            nextIndex++;
        }
        return nextIndex;
    }

    private static int getPreviousIndex(List<Integer> ranks, int previousIndex) {
        while (previousIndex >= 0 && ranks.get(previousIndex) == DUMMY_RANK) {
            previousIndex--;
        }
        return previousIndex;
    }

    int addTokensAndGetCount(int maxTokenCount, boolean keepEncodings, byte[] utf8Bytes, List<Integer> out, ArrayList<Integer> ranks) {
        ByteArrayWrapper match = new ByteArrayWrapper(utf8Bytes);
        int encoded = encode(match);
        if (encoded != MAX_RANK) {
            if (keepEncodings) {
                out.add(encoded);
            }
            return 1;
        } else {
            int length = match.length();
            if (length < VERY_LARGE_TOKENIZER_BYTE_THRESHOLD) {
                return calculateTokensSmall(maxTokenCount, keepEncodings, out, ranks, match, length);
            } else {
                return calculateTokensLarge(this, maxTokenCount, keepEncodings, out, match, length);
            }
        }
    }

    private int calculateTokensSmall(int maxTokenCount, boolean keepEncodings, List<Integer> out, ArrayList<Integer> ranks, ByteArrayWrapper match, int length) {
        assert length > 1 : "Already filtered out";
        ranks.clear();
        ranks.ensureCapacity(length + 1);

        int validRanks = 0;
        int minRankIndex = -1;
        for (int i = 0, minRank = MAX_RANK; i < length + 1; i++) {
            int encoded = encode(match, i, i + 2);
            if (encoded != MAX_RANK) {
                validRanks++;
                if (encoded < minRank) {
                    minRankIndex = i;
                    minRank = encoded;
                }
            }
            ranks.add(encoded);
        }
        int tokenCount = mergeBytesAndGetTokenCount(match, length, ranks, validRanks, minRankIndex);
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

    int mergeBytesAndGetTokenCount(ByteArrayWrapper piece, int length, List<Integer> ranks, int validRanks, int minRankIndex) {
        assert getMinRankIndex(ranks) == minRankIndex;
        while (validRanks > 0) {
            assert minRankIndex >= 0;

            int previousIndex = getPreviousIndex(ranks, minRankIndex - 1);
            int nextIndex = getNextIndex(ranks, minRankIndex + 1);
            int nextNextIndex = getNextIndex(ranks, nextIndex + 1);
            int nextNextNextIndex = getNextIndex(ranks, nextNextIndex + 1);

            if (previousIndex >= 0) {
                assert ranks.get(previousIndex) != DUMMY_RANK;
                int newRank = encode(piece, previousIndex, nextNextIndex);
                int oldRank = ranks.set(previousIndex, newRank);
                if ((newRank == MAX_RANK) != (oldRank == MAX_RANK)) {
                    validRanks -= (newRank == MAX_RANK) ? 1 : -1;
                }
            }
            assert ranks.get(minRankIndex) != DUMMY_RANK;
            int newRank = encode(piece, minRankIndex, nextNextNextIndex);
            int oldRank = ranks.set(minRankIndex, newRank);
            if ((newRank == MAX_RANK) != (oldRank == MAX_RANK)) {
                validRanks--;
            }

            int oldDeletedRank = ranks.set(nextIndex, DUMMY_RANK);
            if (oldDeletedRank != MAX_RANK) {
                validRanks--;
            }

            length--;

            minRankIndex = getMinRankIndex(ranks);
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
        if (end > piece.length()) {
            return MAX_RANK;
        } else if (end - start == piece.length()) {
            return encode(piece);
        } else {
            return encode(piece.getBytesBetween(start, end));
        }
    }
}