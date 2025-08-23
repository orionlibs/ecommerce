package de.hybris.platform.cms2.exceptions;

import de.hybris.platform.servicelayer.exceptions.BusinessException;

public class ItemNotFoundException extends BusinessException
{
    public ItemNotFoundException(String message)
    {
        super(message);
    }


    public ItemNotFoundException(Throwable cause)
    {
        super(cause);
    }


    public ItemNotFoundException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
