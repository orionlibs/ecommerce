package de.hybris.platform.cockpit.services.config;

public class UIConfigurationException extends RuntimeException
{
    public UIConfigurationException()
    {
    }


    public UIConfigurationException(String message)
    {
        super(message);
    }


    public UIConfigurationException(Throwable cause)
    {
        super(cause);
    }


    public UIConfigurationException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
