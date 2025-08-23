package com.hybris.datahub.model;

public class InvalidAttributeException extends RuntimeException
{
    private static final long serialVersionUID = 5062918138841701922L;


    public InvalidAttributeException(String msg)
    {
        super(msg);
    }


    public InvalidAttributeException()
    {
    }
}
