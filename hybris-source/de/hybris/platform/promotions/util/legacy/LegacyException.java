package de.hybris.platform.promotions.util.legacy;

public class LegacyException extends RuntimeException
{
    private static final long serialVersionUID = 1850905883340508186L;


    public LegacyException()
    {
    }


    public LegacyException(String s)
    {
        super(s);
    }


    public LegacyException(String message, Throwable cause)
    {
        super(message, cause);
    }


    public LegacyException(Throwable cause)
    {
        super(cause);
    }
}
