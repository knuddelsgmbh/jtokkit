package com.knuddels.jtokkit;


import com.knuddels.jtokkit.api.IntArrayList;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import static com.knuddels.jtokkit.TokenEncoder.MAX_RANK;
import static java.util.Objects.requireNonNull;

final class TokenEncoderLarge {
    static int calculateTokensLarge(TokenEncoder tokenEncoder, int maxTokenCount, boolean keepEncodings, IntArrayList out, ByteArrayWrapper match) {
        int length = match.length();
        assert length > 1 : "Already filtered out";

        TreeMap<Integer, LinkedHashMap<Integer, RankNode>> rankMap = new TreeMap<>();

        RankNode head = null;
        RankNode prevNode = null;
        for (int i = 0; i < length + 1; i++) {
            int encoded = tokenEncoder.encode(match, i, i + 2);
            RankNode node = new RankNode(encoded, i);
            if (head == null) {
                head = node;
            } else {
                prevNode.next = node;
                node.prev = prevNode;
            }
            prevNode = node;

            rankMap.computeIfAbsent(encoded, k -> new LinkedHashMap<>()).put(i, node);
        }
        assert rankMap.containsKey(MAX_RANK);
        while (rankMap.size() > 1) {
            for (Iterator<RankNode> it = rankMap.pollFirstEntry().getValue().values().iterator(); it.hasNext(); ) {
                RankNode minNode = it.next();
                int minRank = minNode.rank;
                assert minRank != MAX_RANK;

                RankNode previousNode = minNode.prev;
                RankNode nextNode = minNode.next;
                RankNode nextNextNode = nextNode != null ? nextNode.next : null;
                RankNode nextNextNextNode = nextNextNode != null ? nextNextNode.next : null;

                if (previousNode != null) {
                    int newRank = tokenEncoder.encode(match, previousNode.index, nextNextNode != null ? nextNextNode.index : Integer.MAX_VALUE);
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
                if (nextNode != null && nextNextNode != null) {
                    nextNextNode.prev = minNode;
                    if (nextNode.rank != MAX_RANK) {
                        if (nextNode.rank != minRank) {
                            removeNode(rankMap.get(nextNode.rank), rankMap, nextNode);
                        } else {
                            it.next();
                        }
                    }
                }

                length--;
            }
        }
        assert rankMap.firstEntry().getValue().values().iterator().next().rank == MAX_RANK;

        if (keepEncodings) {
            while (head.next != null && out.size() < maxTokenCount) {
                int token = tokenEncoder.encode(match, head.index, head.next.index);
                assert token != MAX_RANK : "Token should not be MAX_RANK";
                out.add(token);
                head = head.next;
            }
        }

        return length;
    }

    static void removeNode(Map<Integer, RankNode> nodeMap, Map<Integer, ? extends Map<Integer, RankNode>> rankMap, RankNode nextNode) {
        if (requireNonNull(nodeMap).size() == 1) {
            assert nodeMap.containsKey(nextNode.index);
            rankMap.remove(nextNode.rank);
        } else {
            nodeMap.remove(nextNode.index);
        }
    }

    private static class RankNode {
        int rank;
        int index;
        RankNode prev, next;

        RankNode(int rank, int index) {
            this.rank = rank;
            this.index = index;
        }

        @Override
        public String toString() {
            return "RankNode{rank=" + rank + ", index=" + index + '}';
        }
    }
}