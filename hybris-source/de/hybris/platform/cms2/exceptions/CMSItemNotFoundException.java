package de.hybris.platform.cms2.exceptions;

import de.hybris.platform.servicelayer.exceptions.BusinessException;

public class CMSItemNotFoundException extends BusinessException
{
    private static final long serialVersionUID = -1451559610063495933L;


    public CMSItemNotFoundException(String message, Throwable cause)
    {
        super(message, cause);
    }


    public CMSItemNotFoundException(String message)
    {
        super(message);
    }


    public CMSItemNotFoundException(Throwable cause)
    {
        super(cause);
    }
}
