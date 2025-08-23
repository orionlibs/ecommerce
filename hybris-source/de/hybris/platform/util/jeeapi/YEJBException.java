package de.hybris.platform.util.jeeapi;

public class YEJBException extends RuntimeException
{
    public YEJBException(String s)
    {
        super(s);
    }


    public YEJBException(Exception e)
    {
        super(e);
    }


    public Throwable getCausedByException()
    {
        return getCause();
    }
}
