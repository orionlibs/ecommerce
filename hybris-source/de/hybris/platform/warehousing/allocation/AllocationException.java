package de.hybris.platform.warehousing.allocation;

public class AllocationException extends RuntimeException
{
    private static final long serialVersionUID = -6302207170322132302L;


    public AllocationException(String message)
    {
        super(message);
    }


    public AllocationException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
