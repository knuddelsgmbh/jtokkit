package com.knuddels.jtokkit;

public class DefaultEncodingRegistryTest extends BaseEncodingRegistryTest<DefaultEncodingRegistry> {

	public DefaultEncodingRegistryTest() {
		super(new DefaultEncodingRegistry(), DefaultEncodingRegistry::initializeDefaultEncodings);
	}
}
