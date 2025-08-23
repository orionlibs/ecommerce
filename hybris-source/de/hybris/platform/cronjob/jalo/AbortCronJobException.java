package de.hybris.platform.cronjob.jalo;

public class AbortCronJobException extends Exception
{
    public AbortCronJobException(String message)
    {
        super(message);
    }


    public AbortCronJobException()
    {
    }


    public AbortCronJobException(String message, Throwable cause)
    {
        super(message, cause);
    }


    public AbortCronJobException(Throwable cause)
    {
        super(cause);
    }
}
