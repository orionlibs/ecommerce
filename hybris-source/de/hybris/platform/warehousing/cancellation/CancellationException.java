package de.hybris.platform.warehousing.cancellation;

public class CancellationException extends RuntimeException
{
    private static final long serialVersionUID = 755896214498434805L;


    public CancellationException(String message)
    {
        super(message);
    }


    public CancellationException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
