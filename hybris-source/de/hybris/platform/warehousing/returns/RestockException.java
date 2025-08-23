package de.hybris.platform.warehousing.returns;

public class RestockException extends Exception
{
    private static final long serialVersionUID = 1L;


    public RestockException(String message)
    {
        super(message);
    }


    public RestockException(Exception exception)
    {
        super(exception);
    }
}
