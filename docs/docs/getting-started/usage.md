---
title: Usage
sidebar_position: 2
---

To use JTokkit, first create a new `EncodingRegistry`:

```java
EncodingRegistry registry = Encodings.newDefaultEncodingRegistry();
```

Make sure to keep a reference to the registry, as the creation of the registry is expensive. Creating the registry loads the vocabularies from the classpath. The registry itself handles caching of the loaded encodings. It is thread-safe and can safely be used concurrently by multiple components.

If you do not want to automatically load all vocabularies of all encodings on registry creation, you can use the following lazy loading registry.

```java
EncodingRegistry registry = Encodings.newLazyEncodingRegistry();
```

This encoding registry only loads the vocabularies from encodings that are actually accessed. Vocabularies are only
loaded once on first accessed. As with the default encoding registry, make sure to keep a reference to the registry
to make use of the in-built caching of the vocabularies. It is thread-safe and can safely be used concurrently by
multiple components.

## Getting an encoding from the registry

You can use the registry to get the encodings you need:

```java
// Get encoding via type-safe enum
Encoding encoding = registry.getEncoding(EncodingType.CL100K_BASE);

// Get encoding via string name
Optional<Encoding> encoding = registry.getEncoding("cl100k_base");

// Get encoding for a specific model via type-safe enum
Encoding encoding = registry.getEncodingForModel(ModelType.GPT_4);

// Get encoding for a specific model via string name
Optional<Encoding> encoding = registry.getEncodingForModel("gpt_4");
```

## Encoding and decoding text

You can use an `Encoding` to encode and decode text:

```java
IntArrayList encoded = encoding.encode("This is a sample sentence.");
// encoded = [2028, 374, 264, 6205, 11914, 13]

String decoded = encoding.decode(encoded);
// decoded = "This is a sample sentence."
```

The encoding is also fully thread-safe and can be used concurrently by multiple components.

:::info

Note that the library does not support encoding of special tokens. Special tokens are artificial tokens used to unlock capabilities from a model, such as fill-in-the-middle. If the `Encoding#encode` method encounters a special token in the input text, it will throw an `UnsupportedOperationException`.

If you want to encode special tokens as if they were normal text, you can use `Encoding#encodeOrdinary` instead:

```java
encoding.encode("hello <|endoftext|> world");
// raises an UnsupportedOperationException

encoding.encodeOrdinary("hello <|endoftext|> world");
// returns [15339, 83739, 8862, 728, 428, 91, 29, 1917]
```

:::

## Counting tokens

If all you want is the amount of tokens the text encodes to, you can use the shorthand method `Encoding#countTokens` or `Encoding#countTokensOrdinary`. These methods are faster than the corresponding `encode` methods.

```java
int tokenCount = encoding.countTokens("This is a sample sentence.");
// tokenCount = 6

int tokenCount = encoding.countTokensOrdinary("hello <|endoftext|> world");
// tokenCount = 8
```

## Encoding text with truncation

If you want to only encode up until a specified amount of `maxTokens` and truncate after that amount, you can use `Encoding#encode(String, int)` or `Encoding#encodeOrdinary(String, int)`. These methods will truncate the encoded tokens to the specified length. They will automatically handle unicode characters that were split in half by the truncation by removing those tokens from the end of the list.

```java
IntArrayList encoded = encoding.encode("This is a sample sentence.", 3);
// encoded = [2028, 374, 264]

String decoded = encoding.decode(encoded);
// decoded = "This is a"

IntArrayList encoded = encoding.encode("I love 🍕", 4);
// encoded = [40, 3021]

String decoded = encoding.decode(encoded);
// decoded = "I love"
```
