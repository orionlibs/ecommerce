package com.hybris.datahub.shutdown;

public class ShutdownException extends IllegalStateException
{
    private static final long serialVersionUID = 1545386458862383353L;


    public ShutdownException(String s)
    {
        super(s);
    }
}
