package de.hybris.bootstrap.beangenerator;

public class BeanGenerationException extends RuntimeException
{
    public BeanGenerationException()
    {
    }


    public BeanGenerationException(String message)
    {
        super(message);
    }


    public BeanGenerationException(Throwable exception)
    {
        super(exception);
    }


    public BeanGenerationException(String message, Throwable exception)
    {
        super(message, exception);
    }
}
