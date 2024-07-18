package com.knuddels.jtokkit.api;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum EncodingType {
    R50K_BASE("r50k_base"),
    P50K_BASE("p50k_base"),
    P50K_EDIT("p50k_edit"),
    CL100K_BASE("cl100k_base"),
    O200K_BASE("o200k_base");

    private static final Map<String, EncodingType> nameToEncodingType = Arrays.stream(values())
            .collect(Collectors.toMap(EncodingType::getName, Function.identity()));

    private final String name;

    EncodingType(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static Optional<EncodingType> fromName(final String name) {
        return Optional.ofNullable(nameToEncodingType.get(name));
    }
}
