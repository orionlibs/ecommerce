package de.hybris.platform.ordersplitting;

public class ConsignmentCreationException extends Exception
{
    public ConsignmentCreationException()
    {
    }


    public ConsignmentCreationException(String message)
    {
        super(message);
    }


    public ConsignmentCreationException(Throwable cause)
    {
        super(cause);
    }


    public ConsignmentCreationException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
