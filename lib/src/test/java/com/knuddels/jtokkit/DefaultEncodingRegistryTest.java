package com.knuddels.jtokkit;

class DefaultEncodingRegistryTest extends BaseEncodingRegistryTest<DefaultEncodingRegistry> {

    DefaultEncodingRegistryTest() {
        super(new DefaultEncodingRegistry(), DefaultEncodingRegistry::initializeDefaultEncodings);
    }
}
