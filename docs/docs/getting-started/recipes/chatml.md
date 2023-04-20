---
title: Counting Tokens for ChatML
sidebar_position: 1
---

If you are using the OpenAI chat models, you need to account for additional tokens that are added to the input text. This recipe shows how to do that. It is based on this [OpenAI Cookbook example](https://github.com/openai/openai-cookbook/blob/main/examples/How_to_count_tokens_with_tiktoken.ipynb).

```java
private int countMessageTokens(
		EncodingRegistry registry,
		String model,
		List<ChatMessage> messages // consists of role, content and an optional name
) {
	Encoding encoding = registry.getEncodingForModel(model).orElseThrow();
	int tokensPerMessage;
	int tokensPerName
	if (model.startsWith("gpt-4")) {
		tokensPerMessage = 3;
		tokensPerName = 1;
	} else if (model.startsWith("gpt-3.5-turbo")) {
		tokensPerMessage = 4; // every message follows <|start|>{role/name}\n{content}<|end|>\n
		tokensPerName = -1; // if there's a name, the role is omitted
	} else {
		throw new IllegalArgumentException("Unsupported model: " + model);
	}

	int sum = 0;
	for (final var message : messages) {
		sum += tokensPerMessage;
		sum += encoding.countTokens(message.getContent());
		sum += encoding.countTokens(message.getRole());
		if (message.hasName()) {
			sum += encoding.countTokens(message.getName());
			sum += tokensPerName;
		}
	}

	sum += 3; // every reply is primed with <|start|>assistant<|message|>

	return sum;
}
```