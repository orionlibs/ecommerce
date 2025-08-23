package com.hybris.datahub.client;

public abstract class SecurityCredentialsInfo
{
    public abstract void applyTo(ClientConfiguration paramClientConfiguration);


    public abstract boolean isValid();
}
