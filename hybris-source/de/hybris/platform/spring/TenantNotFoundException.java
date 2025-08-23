package de.hybris.platform.spring;

public class TenantNotFoundException extends RuntimeException
{
    public TenantNotFoundException(String the)
    {
        super(the);
    }
}
