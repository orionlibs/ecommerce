package de.hybris.platform.cms2.exceptions;

import de.hybris.platform.servicelayer.exceptions.BusinessException;

public class ItemRollbackException extends BusinessException
{
    public ItemRollbackException(String message)
    {
        super(message);
    }


    public ItemRollbackException(Throwable cause)
    {
        super(cause);
    }


    public ItemRollbackException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
