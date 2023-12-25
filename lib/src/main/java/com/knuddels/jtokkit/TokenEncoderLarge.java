package com.knuddels.jtokkit;


import java.util.List;
import java.util.TreeMap;

import static com.knuddels.jtokkit.TokenEncoder.MAX_RANK;

final class TokenEncoderLarge {
    static int addTokensAndGetCountLarge(TokenEncoder tokenEncoder, int maxTokenCount, boolean keepEncodings, List<Integer> out, ByteArrayWrapper match, int length) {
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
            RankNode minNode = rankMap.firstEntry().getValue().firstEntry().getValue();
            assert minNode.rank != MAX_RANK;

            RankNode previousNode = minNode.prev;
            RankNode nextNode = minNode.next;
            RankNode nextNextNode = nextNode != null ? nextNode.next : null;
            RankNode nextNextNextNode = nextNextNode != null ? nextNextNode.next : null;

            if (previousNode != null) {
                int newRank = tokenEncoder.encode(match, previousNode.index, nextNextNode != null ? nextNextNode.index : Integer.MAX_VALUE);
                if ((newRank == MAX_RANK) != (previousNode.rank == MAX_RANK)) {
                    validRanks -= (newRank == MAX_RANK) ? 1 : -1;
                }
                removeNode(rankMap, previousNode);
                previousNode.rank = newRank;
                rankMap.computeIfAbsent(newRank, k -> new TreeMap<>()).put(previousNode.index, previousNode);
            }

            int newRank = tokenEncoder.encode(match, minNode.index, nextNextNextNode != null ? nextNextNextNode.index : Integer.MAX_VALUE);
            if ((newRank == MAX_RANK) != (minNode.rank == MAX_RANK)) {
                validRanks--;
            }
            removeNode(rankMap, minNode);
            minNode.rank = newRank;
            rankMap.computeIfAbsent(newRank, k -> new TreeMap<>()).put(minNode.index, minNode);

            minNode.next = nextNextNode;
            if (nextNode != null) {
                if (nextNextNode != null) {
                    nextNextNode.prev = minNode;
                }
                if (nextNode.rank != MAX_RANK) {
                    validRanks--;
                }
                removeNode(rankMap, nextNode);
            }

            length--;
        }
        assert rankMap.firstEntry().getValue().firstEntry().getValue().rank == MAX_RANK;

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

    static void removeNode(TreeMap<Integer, TreeMap<Integer, RankNode>> rankMap, RankNode nextNode) {
        TreeMap<Integer, RankNode> nodeMap = rankMap.get(nextNode.rank);
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
    }
}