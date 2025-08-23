package de.hybris.platform.adaptivesearchbackoffice.facades;

public class AsFacadeRuntimeException extends RuntimeException
{
    private static final long serialVersionUID = 1L;


    public AsFacadeRuntimeException()
    {
    }


    public AsFacadeRuntimeException(String message)
    {
        super(message);
    }


    public AsFacadeRuntimeException(String message, Throwable cause)
    {
        super(message, cause);
    }


    public AsFacadeRuntimeException(Throwable cause)
    {
        super(cause);
    }
}
