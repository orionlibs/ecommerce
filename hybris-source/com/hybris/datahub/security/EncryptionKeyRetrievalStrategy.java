package com.hybris.datahub.security;

public interface EncryptionKeyRetrievalStrategy<T>
{
    T getKey();
}
