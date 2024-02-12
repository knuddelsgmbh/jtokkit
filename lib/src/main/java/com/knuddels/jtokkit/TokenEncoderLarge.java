package com.knuddels.jtokkit;


import static com.knuddels.jtokkit.TokenEncoder.MAX_RANK;
import static java.util.Objects.requireNonNull;

import com.knuddels.jtokkit.api.IntArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

final class TokenEncoderLarge {
    static int calculateTokensLarge(TokenEncoder tokenEncoder, int maxTokenCount, boolean keepEncodings, IntArrayList out, ByteArrayWrapper match) {
        TreeMap<Integer, LinkedHashMap<Integer, RankNode>> rankMap = new TreeMap<>();

        RankNode prev = null;
        for (int i = 0; i < match.length() + 1; i++) {
            int rank = tokenEncoder.encode(match, i, i + 2);
            RankNode node = new RankNode(rank, i, prev);
            if (prev != null) {
                prev.next = node;
            }
            prev = node;

            rankMap.computeIfAbsent(rank, k -> new LinkedHashMap<>()).put(i, node);
        }
        assert rankMap.containsKey(MAX_RANK);

        int tokenCount = match.length();
        while (rankMap.size() > 1) {
            for (Iterator<RankNode> it = rankMap.pollFirstEntry().getValue().values().iterator(); it.hasNext(); ) {
                RankNode minNode = it.next();
                int minRank = minNode.rank;
                assert minRank != MAX_RANK;

                RankNode previousNode = minNode.prev,
                        nextNode = minNode.next,
                        nextNextNode = nextNode.next,
                        nextNextNextNode = nextNextNode.next;

                if (previousNode != null) {
                    int newRank = tokenEncoder.encode(match, previousNode.index, nextNextNode.index);
                    if (previousNode.rank != newRank) {
                        assert previousNode.rank != minRank;
                        removeNode(rankMap.get(previousNode.rank), rankMap, previousNode);
                        previousNode.rank = newRank;
                        rankMap.computeIfAbsent(newRank, k -> new LinkedHashMap<>()).put(previousNode.index, previousNode);
                    }
                }

                int newRank = tokenEncoder.encode(match, minNode.index, nextNextNextNode != null ? nextNextNextNode.index : Integer.MAX_VALUE);
                minNode.rank = newRank;
                rankMap.computeIfAbsent(newRank, k -> new LinkedHashMap<>()).put(minNode.index, minNode);

                minNode.next = nextNextNode;
                nextNextNode.prev = minNode;
                if (nextNode.rank != MAX_RANK) {
                    if (nextNode.rank != minRank) {
                        removeNode(rankMap.get(nextNode.rank), rankMap, nextNode);
                    } else {
                        it.next();
                    }
                }

                tokenCount--;
            }
        }

        if (keepEncodings) {
            for (RankNode head = rankMap.get(MAX_RANK).get(0); head.next != null && out.size() < maxTokenCount; head = head.next) {
                int token = tokenEncoder.encode(match, head.index, head.next.index);
                assert token != MAX_RANK : "Token should not be MAX_RANK";
                out.add(token);
            }
        }

        return tokenCount;
    }

    static void removeNode(Map<Integer, RankNode> nodeMap, Map<Integer, ? extends Map<Integer, RankNode>> rankMap, RankNode node) {
        if (requireNonNull(nodeMap).size() == 1) {
            assert nodeMap.containsKey(node.index);
            rankMap.remove(node.rank);
        } else {
            nodeMap.remove(node.index);
        }
    }

    private static class RankNode {
        int rank;
        int index;
        RankNode prev, next;

        RankNode(int rank, int index, RankNode prev) {
            this.rank = rank;
            this.index = index;
            this.prev = prev;
        }

        @Override
        public String toString() {
            return "RankNode{rank=" + rank + ", index=" + index + '}';
        }
    }
}