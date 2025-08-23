package com.hybris.datahub.security;

public interface AttributeEncryptionStrategy
{
    String encrypt(String paramString) throws EncryptionStrategyException;


    String decrypt(String paramString) throws EncryptionStrategyException;


    boolean isEnabled();
}
