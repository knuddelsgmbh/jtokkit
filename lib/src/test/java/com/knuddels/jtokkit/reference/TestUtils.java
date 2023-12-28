package com.knuddels.jtokkit.reference;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class TestUtils {

    static List<Integer> parseEncodingString(String encodingString) {
        return Arrays.stream(
                        encodingString.substring(1, encodingString.length() - 1)
                                .replaceAll(" ", "")
                                .split(",")
                ).map(Integer::parseInt)
                .collect(Collectors.toList());
    }
}
