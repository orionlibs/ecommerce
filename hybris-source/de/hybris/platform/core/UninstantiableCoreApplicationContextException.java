package de.hybris.platform.core;

public class UninstantiableCoreApplicationContextException extends Exception
{
    public UninstantiableCoreApplicationContextException(String msg, Exception e)
    {
        super(msg, e);
    }
}
