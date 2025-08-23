package de.hybris.platform.servicelayer.exceptions;

public class ModelTypeNotSupportedException extends IllegalArgumentException
{
    private final String type;


    public ModelTypeNotSupportedException(String message, String unsupportedType)
    {
        super(message);
        this.type = unsupportedType;
    }


    public String getUnsupportedType()
    {
        return this.type;
    }
}
