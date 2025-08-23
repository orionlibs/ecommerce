package de.hybris.platform.ticket.service;

public class UnsupportedAttachmentException extends RuntimeException
{
    public UnsupportedAttachmentException()
    {
    }


    public UnsupportedAttachmentException(String message)
    {
        super(message);
    }


    public UnsupportedAttachmentException(String message, Throwable cause)
    {
        super(message, cause);
    }


    public UnsupportedAttachmentException(Throwable cause)
    {
        super(cause);
    }
}
