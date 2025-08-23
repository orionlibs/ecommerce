package de.hybris.platform.servicelayer.dto.converter;

public class ConversionException extends RuntimeException
{
    private static final long serialVersionUID = -2280904009639576411L;


    public ConversionException(String msg, Throwable cause)
    {
        super(msg, cause);
    }


    public ConversionException(String msg)
    {
        super(msg);
    }
}
