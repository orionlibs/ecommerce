package de.hybris.platform.cms2.exceptions;

import de.hybris.platform.servicelayer.exceptions.BusinessException;

public class CMSVersionNotFoundException extends BusinessException
{
    public CMSVersionNotFoundException(String message, Throwable cause)
    {
        super(message, cause);
    }


    public CMSVersionNotFoundException(String message)
    {
        super(message);
    }


    public CMSVersionNotFoundException(Throwable cause)
    {
        super(cause);
    }
}
