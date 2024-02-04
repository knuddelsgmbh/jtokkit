package com.knuddels.jtokkit.reference;

import com.knuddels.jtokkit.api.IntArrayList;

import java.util.Arrays;
import java.util.List;

class TestUtils {

    static IntArrayList parseEncodingString(String encodingString) {
        List<Integer> list = Arrays.stream(
                        encodingString.substring(1, encodingString.length() - 1)
                                .replaceAll(" ", "")
                                .split(",")
                ).map(Integer::parseInt)
                .toList();
        var result = new IntArrayList(list.size());
        list.forEach(result::add);
        return result;
    }
}
