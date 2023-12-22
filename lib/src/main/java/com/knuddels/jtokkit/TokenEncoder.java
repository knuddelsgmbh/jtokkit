package com.knuddels.jtokkit;


import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

final class TokenEncoder {
    public static final int DUMMY_RANK = Integer.MAX_VALUE;
    public static final int MAX_RANK = Integer.MAX_VALUE - 1;
    private final Map<ImmutableByteArray, Integer>[] encoders;
    private int length = 0;

    public TokenEncoder(Map<byte[], Integer> encoder) {
        if (!encoder.isEmpty()) {
            TreeMap<Integer, Map<ImmutableByteArray, Integer>> tempEncoders = new TreeMap<>();
            encoder.forEach((k, v) -> {
                length++;
                ImmutableByteArray key = ImmutableByteArray.from(k);
                tempEncoders.computeIfAbsent(k.length, integer -> new ConcurrentHashMap<>()).put(key, v);
            });
            //noinspection unchecked
            encoders = new ConcurrentHashMap[tempEncoders.lastKey() + 1];
            tempEncoders.forEach((k, v) -> encoders[k] = v);
        } else {
            //noinspection unchecked
            encoders = new Map[0]; // for testing
        }
    }

    public static int getMinRankIndex(List<Integer> ranks) {
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

    public static int getNextIndex(List<Integer> ranks, int nextIndex) {
        while (nextIndex < ranks.size() && ranks.get(nextIndex) == DUMMY_RANK) {
            nextIndex++;
        }
        return nextIndex;
    }

    public static int getPreviousIndex(List<Integer> ranks, int previousIndex) {
        while (previousIndex >= 0 && ranks.get(previousIndex) == DUMMY_RANK) {
            previousIndex--;
        }
        return previousIndex;
    }

    public int addTokensAndGetCount(Integer maxTokenCount, byte[] utf8Bytes, List<Integer> out, List<Integer> ranks) {
        ImmutableByteArray match = ImmutableByteArray.from(utf8Bytes);
        int encoded = encode(match);
        if (encoded != MAX_RANK) {
            out.add(encoded);
            return 1;
        } else {
            int length = match.length();
            return addTokensAndGetCount(maxTokenCount, out, ranks, match, length);
        }
    }

    private int addTokensAndGetCount(Integer maxTokenCount, List<Integer> out, List<Integer> ranks, ImmutableByteArray match, int length) {
        int validRanks = initRanks(match, length, ranks);
        int tokenCount = mergeBytesAndGetTokenCount(match, length, ranks, validRanks);
        for (int start = 0, end = 1; end < ranks.size() && (maxTokenCount == null || out.size() < maxTokenCount); end++) {
            if (ranks.get(end) != DUMMY_RANK) {
                int token = encode(match, start, end);
                assert token != MAX_RANK : "Token should not be MAX_RANK";
                out.add(token);
                start = end;
            }
        }
        return tokenCount;
    }

    int initRanks(ImmutableByteArray piece, int tokenCount, List<Integer> ranks) {
        assert tokenCount > 1 : "Already filtered out";
        ranks.clear();
        int validRanks = 0;
        for (int i = 0; i < tokenCount + 1; i++) {
            int encoded = encode(piece, i, i + 2);
            if (encoded != MAX_RANK) {
                validRanks++;
            }
            ranks.add(encoded);
        }
        return validRanks;
    }

    int mergeBytesAndGetTokenCount(ImmutableByteArray piece, int length, List<Integer> ranks, int validRanks) {
        assert true;
        while (true) {
            if (validRanks == 0) {
                assert getMinRankIndex(ranks) < 0;
                break;
            }
            int minRankIndex = getMinRankIndex(ranks);
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
        }
        return length;
    }

    int encode(ImmutableByteArray payload) {
        if (payload.length() < encoders.length) {
            Map<ImmutableByteArray, Integer> encoder = encoders[payload.length()];
            return encoder == null ? MAX_RANK : encoder.getOrDefault(payload, MAX_RANK);
        } else {
            return MAX_RANK;
        }
    }

    int encode(ImmutableByteArray piece, int start, int end) {
        if (end > piece.length()) {
            return MAX_RANK;
        } else if (end - start == piece.length()) {
            return encode(piece);
        } else {
            return encode(piece.getBytesBetween(start, end));
        }
    }

    public int length() {
        return length;
    }
}