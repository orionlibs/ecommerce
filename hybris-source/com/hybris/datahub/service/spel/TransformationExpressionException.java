package com.hybris.datahub.service.spel;

public class TransformationExpressionException extends RuntimeException
{
    private static final long serialVersionUID = 8164340196464189780L;


    public TransformationExpressionException(String message)
    {
        super(message);
    }


    public TransformationExpressionException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
