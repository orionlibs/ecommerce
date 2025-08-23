package de.hybris.platform.mediaconversion.conversion;

public class MediaConversionException extends Exception
{
    private static final long serialVersionUID = -780933445093402975L;


    public MediaConversionException(String message, Throwable cause)
    {
        super(message, cause);
    }


    public MediaConversionException(String message)
    {
        super(message);
    }


    public MediaConversionException(Throwable cause)
    {
        super(cause);
    }
}
