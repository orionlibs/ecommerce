package de.hybris.platform.processengine.spring;

import java.io.Serializable;

public class InvalidParameterTypeException extends RuntimeException
{
    private final Class<?> specifiedType;
    private final Class<?> expectedType;
    private final String parameterName;


    public InvalidParameterTypeException(String message, Class<?> specifiedType)
    {
        this(message, null, specifiedType, Serializable.class);
    }


    public InvalidParameterTypeException(String parameterName, Class<?> specifiedType, Class<?> expectedType)
    {
        this("Invalid type for parameter '" + parameterName + "' specified: '" + specifiedType
                        .getClass().getName() + "'; expected '" + expectedType.getClass().getName() + "'.", parameterName, specifiedType, expectedType);
    }


    public InvalidParameterTypeException(String message, String parameterName, Class<?> specifiedType, Class<?> expectedType)
    {
        super(message);
        this.parameterName = parameterName;
        this.specifiedType = specifiedType;
        this.expectedType = expectedType;
    }


    public Class<?> getSpecifiedType()
    {
        return this.specifiedType;
    }


    public Class<?> getExpectedType()
    {
        return this.expectedType;
    }


    public String getParameterName()
    {
        return this.parameterName;
    }
}
