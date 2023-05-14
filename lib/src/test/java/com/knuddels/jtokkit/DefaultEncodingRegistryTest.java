package com.knuddels.jtokkit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DefaultEncodingRegistryTest extends BaseEncodingRegistryTest {

	@BeforeEach
	void initializeDefaultEncodingsRegistry() {
		registry = new DefaultEncodingRegistry();
	}

	@Test
	@Override
	public void getEncodingReturnsCorrectEncoding() {
		((DefaultEncodingRegistry) registry).initializeDefaultEncodings();

		super.getEncodingReturnsCorrectEncoding();
	}

	@Test
	@Override
	void getEncodingByNameReturnsCorrectEncoding() {
		((DefaultEncodingRegistry) registry).initializeDefaultEncodings();

		super.getEncodingByNameReturnsCorrectEncoding();
	}

	@Test
	@Override
	public void getEncodingForModelReturnsCorrectsEncoding() {
		((DefaultEncodingRegistry) registry).initializeDefaultEncodings();

		super.getEncodingForModelReturnsCorrectsEncoding();
	}

	@Test
	@Override
	public void getEncodingForModelByNameReturnsCorrectEncoding() {
		((DefaultEncodingRegistry) registry).initializeDefaultEncodings();

		super.getEncodingForModelByNameReturnsCorrectEncoding();
	}

	@Test
	@Override
	public void getEncodingForModelByPrefixReturnsCorrectEncodingForGpt432k() {
		((DefaultEncodingRegistry) registry).initializeDefaultEncodings();

		super.getEncodingForModelByPrefixReturnsCorrectEncodingForGpt432k();
	}

	@Test
	@Override
	public void getEncodingForModelByPrefixReturnsCorrectEncodingForGpt4() {
		((DefaultEncodingRegistry) registry).initializeDefaultEncodings();

		super.getEncodingForModelByPrefixReturnsCorrectEncodingForGpt4();
	}

	@Test
	@Override
	public void getEncodingForModelByPrefixReturnsCorrectEncodingForGpt3Turbo() {
		((DefaultEncodingRegistry) registry).initializeDefaultEncodings();

		super.getEncodingForModelByPrefixReturnsCorrectEncodingForGpt3Turbo();
	}
}
