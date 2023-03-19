package com.knuddels.jtokkit.api;

public enum ModelType {
	// chat
	GPT_4("gpt-4", EncodingType.CL100K_BASE),
	GPT_3_5_TURBO("gpt-3.5-turbo", EncodingType.CL100K_BASE),

	// text
	TEXT_DAVINCI_003("text-davinci-003", EncodingType.P50K_BASE),
	TEXT_DAVINCI_002("text-davinci-002", EncodingType.P50K_BASE),
	TEXT_DAVINCI_001("text-davinci-001", EncodingType.R50K_BASE),
	TEXT_CURIE_001("text-curie-001", EncodingType.R50K_BASE),
	TEXT_BABBAGE_001("text-babbage-001", EncodingType.R50K_BASE),
	TEXT_ADA_001("text-ada-001", EncodingType.R50K_BASE),
	DAVINCI("davinci", EncodingType.R50K_BASE),
	CURIE("curie", EncodingType.R50K_BASE),
	BABBAGE("babbage", EncodingType.R50K_BASE),
	ADA("ada", EncodingType.R50K_BASE),

	// code
	CODE_DAVINCI_002("code-davinci-002", EncodingType.P50K_BASE),
	CODE_DAVINCI_001("code-davinci-001", EncodingType.P50K_BASE),
	CODE_CUSHMAN_002("code-cushman-002", EncodingType.P50K_BASE),
	CODE_CUSHMAN_001("code-cushman-001", EncodingType.P50K_BASE),
	DAVINCI_CODEX("davinci-codex", EncodingType.P50K_BASE),
	CUSHMAN_CODEX("cushman-codex", EncodingType.P50K_BASE),

	// edit
	TEXT_DAVINCI_EDIT_001("text-davinci-edit-001", EncodingType.P50K_EDIT),
	CODE_DAVINCI_EDIT_001("code-davinci-edit-001", EncodingType.P50K_EDIT),

	// embeddings
	TEXT_EMBEDDING_ADA_002("text-embedding-ada-002", EncodingType.CL100K_BASE),

	// old embeddings
	TEXT_SIMILARITY_DAVINCI_001("text-similarity-davinci-001", EncodingType.R50K_BASE),
	TEXT_SIMILARITY_CURIE_001("text-similarity-curie-001", EncodingType.R50K_BASE),
	TEXT_SIMILARITY_BABBAGE_001("text-similarity-babbage-001", EncodingType.R50K_BASE),
	TEXT_SIMILARITY_ADA_001("text-similarity-ada-001", EncodingType.R50K_BASE),
	TEXT_SEARCH_DAVINCI_DOC_001("text-search-davinci-doc-001", EncodingType.R50K_BASE),
	TEXT_SEARCH_CURIE_DOC_001("text-search-curie-doc-001", EncodingType.R50K_BASE),
	TEXT_SEARCH_BABBAGE_DOC_001("text-search-babbage-doc-001", EncodingType.R50K_BASE),
	TEXT_SEARCH_ADA_DOC_001("text-search-ada-doc-001", EncodingType.R50K_BASE),
	CODE_SEARCH_BABBAGE_CODE_001("code-search-babbage-code-001", EncodingType.R50K_BASE),
	CODE_SEARCH_ADA_CODE_001("code-search-ada-code-001", EncodingType.R50K_BASE);

	public final String name;
	public final EncodingType encodingType;

	ModelType(final String name, final EncodingType encodingType) {
		this.name = name;
		this.encodingType = encodingType;
	}

	public String getName() {
		return name;
	}

	public EncodingType getEncodingType() {
		return encodingType;
	}
}
