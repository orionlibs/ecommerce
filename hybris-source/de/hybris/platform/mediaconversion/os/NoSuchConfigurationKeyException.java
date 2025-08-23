package de.hybris.platform.mediaconversion.os;

public class NoSuchConfigurationKeyException extends Exception
{
    private static final long serialVersionUID = -8074396739388698343L;
    private final String prefix;


    public NoSuchConfigurationKeyException(String message, String prefix, Throwable cause)
    {
        super(message, cause);
        this.prefix = prefix;
    }


    public NoSuchConfigurationKeyException(String message, String prefix)
    {
        this(message, prefix, null);
    }


    public String getPrefix()
    {
        return this.prefix;
    }
}
