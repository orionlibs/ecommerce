package de.hybris.platform.servicelayer.action;

public class ActionException extends RuntimeException
{
    public ActionException()
    {
    }


    public ActionException(Throwable t)
    {
        super(t);
    }


    public ActionException(String s)
    {
        super(s);
    }


    public ActionException(String s, Throwable t)
    {
        super(s, t);
    }
}
