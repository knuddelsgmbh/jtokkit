---
title: Introduction
sidebar_position: 1
slug: /getting-started/
---

JTokkit is a fast and efficient tokenizer designed for use in natural language processing tasks using the OpenAI models. It provides an easy-to-use interface for tokenizing input text, for example for counting required tokens in preparation of requests to the `gpt-3.5-turbo` model. This library resulted out of the need to have similar capacities in the JVM ecosystem as the library [tiktoken](https://github.com/openai/tiktoken) provides for Python.

## Features

✅ Implements encoding and decoding via `r50k_base`, `p50k_base`, `p50k_edit` and `cl100k_base`

✅ Easy-to-use API

✅ Easy extensibility for custom encoding algorithms

✅ **Zero** Dependencies

✅ Supports Java 8 and above

✅ Fast and efficient performance

## Performance
JTokkit reaches 2-3 times the throughput of a comparable tokenizer. Take a look in the [benchmarks](https://github.com/knuddelsgmbh/jtokkit/tree/main/benchmark) for more details.

## Installation

You can install JTokkit by adding the following dependency to your Maven project:

```xml
<dependency>
    <groupId>com.knuddels</groupId>
    <artifactId>jtokkit</artifactId>
    <version>0.4.0</version>
</dependency>
```

Or alternatively using Gradle:

```groovy
dependencies {
	implementation 'com.knuddels:jtokkit:0.4.0'
}
```
