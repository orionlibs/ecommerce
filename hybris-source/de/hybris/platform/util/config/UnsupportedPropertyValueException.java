package de.hybris.platform.util.config;

public class UnsupportedPropertyValueException extends RuntimeException
{
    public UnsupportedPropertyValueException(String info, String property, String value)
    {
        super(createMessage(info, property, value));
    }


    public UnsupportedPropertyValueException(String info, String property, String value, Throwable cause)
    {
        super(createMessage(info, property, value), cause);
    }


    private static final String createMessage(String info, String property, String value)
    {
        return info + "; property=" + info + "; value=" + property;
    }
}
