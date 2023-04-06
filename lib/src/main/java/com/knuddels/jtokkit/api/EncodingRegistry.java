package com.knuddels.jtokkit.api;


import java.util.Optional;

/**
 * The EncodingRegistry is used to register custom encodings and to retrieve
 * encodings by name or type. The out-of-the-box supported encodings are registered automatically.
 */
public interface EncodingRegistry {

	/**
	 * Returns the encoding with the given name, if it exists. Otherwise, returns an empty Optional.
	 * Prefer using {@link #getEncoding(EncodingType)} or {@link #getEncodingForModel(ModelType)} for
	 * built-in encodings.
	 *
	 * @param encodingName the name of the encoding
	 * @return the encoding, if it exists
	 */
	Optional<Encoding> getEncoding(String encodingName);

	/**
	 * Returns the encoding with the given type.
	 *
	 * @param encodingType the type of the encoding
	 * @return the encoding
	 */
	Encoding getEncoding(EncodingType encodingType);

	/**
	 * Returns the encoding that is used for the given model type, if it exists. Otherwise, returns an
	 * empty Optional. Prefer using {@link #getEncodingForModel(ModelType)} for built-in encodings.
	 * <p>
	 * Note that you can use this method to retrieve the correct encodings for snapshots of models, for
	 * example "gpt-4-0314" or "gpt-3.5-turbo-0301".
	 *
	 * @param modelName the name of the model to get the encoding for
	 * @return the encoding, if it exists
	 */
	Optional<Encoding> getEncodingForModel(String modelName);

	/**
	 * Returns the encoding that is used for the given model type.
	 *
	 * @param modelType the model type
	 * @return the encoding
	 */
	Encoding getEncodingForModel(ModelType modelType);

	/**
	 * Registers a new byte pair encoding with the given name. The encoding must be thread-safe.
	 *
	 * @param parameters the parameters for the encoding
	 * @return the registry for method chaining
	 * @see GptBytePairEncodingParams
	 * @throws IllegalArgumentException if the encoding name is already registered
	 */
	EncodingRegistry registerGptBytePairEncoding(GptBytePairEncodingParams parameters);

	/**
	 * Registers a new custom encoding with the given name. The encoding must be thread-safe.
	 *
	 * @param encoding the encoding
	 * @return the registry for method chaining
	 * @throws IllegalArgumentException if the encoding name is already registered
	 */
	EncodingRegistry registerCustomEncoding(Encoding encoding);
}
