package com.knuddels.jtokkit.api;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ModelType {
	// chat
	GPT_4("gpt-4", EncodingType.CL100K_BASE, 8192),
	GPT_4_32K("gpt-4-32k", EncodingType.CL100K_BASE, 32768),
	GPT_3_5_TURBO("gpt-3.5-turbo", EncodingType.CL100K_BASE, 4097),
	GPT_3_5_TURBO_16K("gpt-3.5-turbo-16k", EncodingType.CL100K_BASE, 16384),

	// text
	TEXT_DAVINCI_003("text-davinci-003", EncodingType.P50K_BASE, 4097),
	TEXT_DAVINCI_002("text-davinci-002", EncodingType.P50K_BASE, 4097),
	TEXT_DAVINCI_001("text-davinci-001", EncodingType.R50K_BASE, 2049),
	TEXT_CURIE_001("text-curie-001", EncodingType.R50K_BASE, 2049),
	TEXT_BABBAGE_001("text-babbage-001", EncodingType.R50K_BASE, 2049),
	TEXT_ADA_001("text-ada-001", EncodingType.R50K_BASE, 2049),
	DAVINCI("davinci", EncodingType.R50K_BASE, 2049),
	CURIE("curie", EncodingType.R50K_BASE, 2049),
	BABBAGE("babbage", EncodingType.R50K_BASE, 2049),
	ADA("ada", EncodingType.R50K_BASE, 2049),

	// code
	CODE_DAVINCI_002("code-davinci-002", EncodingType.P50K_BASE, 8001),
	CODE_DAVINCI_001("code-davinci-001", EncodingType.P50K_BASE, 8001),
	CODE_CUSHMAN_002("code-cushman-002", EncodingType.P50K_BASE, 2048),
	CODE_CUSHMAN_001("code-cushman-001", EncodingType.P50K_BASE, 2048),
	DAVINCI_CODEX("davinci-codex", EncodingType.P50K_BASE, 4096),
	CUSHMAN_CODEX("cushman-codex", EncodingType.P50K_BASE, 2048),

	// edit
	TEXT_DAVINCI_EDIT_001("text-davinci-edit-001", EncodingType.P50K_EDIT, 3000),
	CODE_DAVINCI_EDIT_001("code-davinci-edit-001", EncodingType.P50K_EDIT, 3000),

	// embeddings
	TEXT_EMBEDDING_ADA_002("text-embedding-ada-002", EncodingType.CL100K_BASE, 8191),

	// old embeddings
	TEXT_SIMILARITY_DAVINCI_001("text-similarity-davinci-001", EncodingType.R50K_BASE, 2046),
	TEXT_SIMILARITY_CURIE_001("text-similarity-curie-001", EncodingType.R50K_BASE, 2046),
	TEXT_SIMILARITY_BABBAGE_001("text-similarity-babbage-001", EncodingType.R50K_BASE, 2046),
	TEXT_SIMILARITY_ADA_001("text-similarity-ada-001", EncodingType.R50K_BASE, 2046),
	TEXT_SEARCH_DAVINCI_DOC_001("text-search-davinci-doc-001", EncodingType.R50K_BASE, 2046),
	TEXT_SEARCH_CURIE_DOC_001("text-search-curie-doc-001", EncodingType.R50K_BASE, 2046),
	TEXT_SEARCH_BABBAGE_DOC_001("text-search-babbage-doc-001", EncodingType.R50K_BASE, 2046),
	TEXT_SEARCH_ADA_DOC_001("text-search-ada-doc-001", EncodingType.R50K_BASE, 2046),
	CODE_SEARCH_BABBAGE_CODE_001("code-search-babbage-code-001", EncodingType.R50K_BASE, 2046),
	CODE_SEARCH_ADA_CODE_001("code-search-ada-code-001", EncodingType.R50K_BASE, 2046);

	private static final Map<String, ModelType> nameToModelType = Arrays.stream(values())
			.collect(Collectors.toMap(ModelType::getName, Function.identity()));

	private final String name;
	private final EncodingType encodingType;
	private final int maxContextLength;

	ModelType(
			final String name,
			final EncodingType encodingType,
			final int maxContextLength
	) {
		this.name = name;
		this.encodingType = encodingType;
		this.maxContextLength = maxContextLength;
	}

	/**
	 * Returns the name of the model type as used by the OpenAI API.
	 *
	 * @return the name of the model type
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the encoding type that is used by this model type.
	 *
	 * @return the encoding type
	 */
	public EncodingType getEncodingType() {
		return encodingType;
	}

	/**
	 * Returns the maximum context length that is supported by this model type. Note that
	 * the maximum context length consists of the amount of prompt tokens and the amount of
	 * completion tokens (where applicable).
	 *
	 * @return the maximum context length for this model type
	 */
	public int getMaxContextLength() {
		return maxContextLength;
	}

	/**
	 * Returns a {@link ModelType} for the given name, or {@link Optional#empty()} if no
	 * such model type exists.
	 *
	 * @param name the name of the model type
	 * @return the model type or {@link Optional#empty()}
	 */
	public static Optional<ModelType> fromName(final String name) {
		return Optional.ofNullable(nameToModelType.get(name));
	}
}
