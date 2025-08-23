package de.hybris.platform.servicelayer.exceptions;

public class AttributeNotSupportedException extends IllegalArgumentException
{
    private final String qualifier;
    private final AttributeError attributeError;


    public AttributeNotSupportedException(String message, String qualifier)
    {
        this(message, qualifier, AttributeError.ATTRIBUTE_DOES_NOT_EXIST);
    }


    public AttributeNotSupportedException(String message, String qualifier, AttributeError attributeError)
    {
        super(message);
        this.qualifier = qualifier;
        this.attributeError = attributeError;
    }


    public AttributeNotSupportedException(String message, Throwable cause, String qualifier)
    {
        this(message, cause, qualifier, AttributeError.ATTRIBUTE_DOES_NOT_EXIST);
    }


    public AttributeNotSupportedException(String message, Throwable cause, String qualifier, AttributeError attributeError)
    {
        super(message, cause);
        this.qualifier = qualifier;
        this.attributeError = attributeError;
    }


    public String getQualifier()
    {
        return this.qualifier;
    }


    public AttributeError getAttributeError()
    {
        return this.attributeError;
    }
}
