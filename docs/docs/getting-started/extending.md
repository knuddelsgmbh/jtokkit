---
title: Extending JTokkit
sidebar_position: 3
---

You may want to extend JTokkit and re-use its registry to support additional byte pair encodings or even completely custom encodings. To do so you have two options.

## Implementing the `Encoding` interface

Implement the `Encoding` interface and register it with the `EncodingRegistry`. Make sure that the name you return from `Encoding#getName` is unique and that your implementation is thread-safe. It will be cached and reused by the `EncodingRegistry`.

```java
EncodingRegistry registry = Encodings.newDefaultEncodingRegistry();
Encoding customEncoding = new CustomEncoding();
registry.register(customEncoding);

// Get the encoding from the registry
Encoding encodingFromRegistry = registry.getEncoding("custom-name");
```

## Adding a new byte pair encoding

You can add a new byte pair encoding by specifying the necessary parameters.

```java
EncodingRegistry registry = Encodings.newDefaultEncodingRegistry();
GptBytePairEncodingParams params = new GptBytePairEncodingParams(
        "custom-name",
        Pattern.compile("some custom pattern"),
        encodingMap,
        specialTokenEncodingMap
);
registry.registerGptBytePairEncoding(params);

// Get the encoding from the registry
Encoding encodingFromRegistry = registry.getEncoding("custom-name");
```

Reference `EncodingFactory` for more details on the parameters and examples on how those parameters are used for the pre-defined encodings.