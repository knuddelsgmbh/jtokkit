package com.knuddels.jtokkit;


import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import static com.knuddels.jtokkit.TokenEncoder.MAX_RANK;

final class TokenEncoderLarge {
    static int calculateTokensLarge(TokenEncoder tokenEncoder, int maxTokenCount, boolean keepEncodings, List<Integer> out, ByteArrayWrapper match, int length) {
        assert length > 1 : "Already filtered out";

        TreeMap<Integer, TreeMap<Integer, RankNode>> rankMap = new TreeMap<>();

        RankNode head = null;
        RankNode prevNode = null;
        int validRanks = 0;
        for (int i = 0; i < length + 1; i++) {
            int encoded = tokenEncoder.encode(match, i, i + 2);
            if (encoded != MAX_RANK) {
                validRanks++;
            }
            RankNode node = new RankNode(encoded, i);
            if (head == null) {
                head = node;
            } else {
                prevNode.next = node;
                node.prev = prevNode;
            }
            prevNode = node;

            rankMap.computeIfAbsent(encoded, k -> new TreeMap<>()).put(i, node);
        }

        while (validRanks > 0) {
            TreeMap<Integer, RankNode> minNodes = rankMap.pollFirstEntry().getValue();
            int firstIndex;
            for (Entry<Integer, RankNode> entry = minNodes.firstEntry(); entry != null; entry = minNodes.ceilingEntry(firstIndex)) {
                RankNode minNode = entry.getValue();
                int minRank = minNode.rank;
                assert minRank != MAX_RANK;

                RankNode previousNode = minNode.prev;
                RankNode nextNode = minNode.next;
                RankNode nextNextNode = nextNode != null ? nextNode.next : null;
                RankNode nextNextNextNode = nextNextNode != null ? nextNextNode.next : null;

                if (previousNode != null) {
                    int newRank = tokenEncoder.encode(match, previousNode.index, nextNextNode != null ? nextNextNode.index : Integer.MAX_VALUE);
                    if (previousNode.rank != newRank) {
                        if ((newRank == MAX_RANK) != (previousNode.rank == MAX_RANK)) {
                            validRanks -= (newRank == MAX_RANK) ? 1 : -1;
                        }
                        assert previousNode.rank != minRank;
                        removeNode(rankMap.get(previousNode.rank), rankMap, previousNode);
                        previousNode.rank = newRank;
                        rankMap.computeIfAbsent(newRank, k -> new TreeMap<>()).put(previousNode.index, previousNode);
                    }
                }

                int newRank = tokenEncoder.encode(match, minNode.index, nextNextNextNode != null ? nextNextNextNode.index : Integer.MAX_VALUE);
                if (newRank == MAX_RANK) {
                    validRanks--;
                }
                firstIndex = minNode.index + 1;
                minNode.rank = newRank;
                rankMap.computeIfAbsent(newRank, k -> new TreeMap<>()).put(minNode.index, minNode);

                minNode.next = nextNextNode;
                if (nextNode != null) {
                    if (nextNextNode != null) {
                        nextNextNode.prev = minNode;
                    }
                    if (nextNode.rank != MAX_RANK) {
                        validRanks--;
                        if (nextNode.rank != minRank) {
                            removeNode(rankMap.get(nextNode.rank), rankMap, nextNode);
                        }
                    }
                    firstIndex = nextNode.index + 1;
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

    static void removeNode(TreeMap<Integer, RankNode> nodeMap, TreeMap<Integer, TreeMap<Integer, RankNode>> rankMap, RankNode nextNode) {
        if (nodeMap.size() == 1) {
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
            return "RankNode{" +
                    "rank=" + rank +
                    ", index=" + index +
                    '}';
        }
    }
}