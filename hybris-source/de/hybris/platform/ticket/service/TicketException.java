package de.hybris.platform.ticket.service;

public class TicketException extends Exception
{
    public TicketException()
    {
    }


    public TicketException(String message)
    {
        super(message);
    }


    public TicketException(String message, Throwable cause)
    {
        super(message, cause);
    }


    public TicketException(Throwable cause)
    {
        super(cause);
    }
}
