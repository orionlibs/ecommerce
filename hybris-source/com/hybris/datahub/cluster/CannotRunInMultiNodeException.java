package com.hybris.datahub.cluster;

public class CannotRunInMultiNodeException extends IllegalStateException
{
    private static final long serialVersionUID = 1545386458862383353L;


    public CannotRunInMultiNodeException(String s)
    {
        super(s);
    }
}
