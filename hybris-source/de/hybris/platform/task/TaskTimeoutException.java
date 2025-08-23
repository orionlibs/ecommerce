package de.hybris.platform.task;

import java.util.Date;

public class TaskTimeoutException extends Exception
{
    private final Date expired;


    public TaskTimeoutException(String message, Date expirationDate)
    {
        super(message);
        this.expired = expirationDate;
    }


    public Date getExpirationDate()
    {
        return this.expired;
    }
}
