package de.hybris.platform.cms2.exceptions;

import de.hybris.platform.servicelayer.exceptions.BusinessException;

public class CMSItemCreateException extends BusinessException
{
    private static final long serialVersionUID = -1451559610063495933L;


    public CMSItemCreateException(String message, Throwable cause)
    {
        super(message, cause);
    }


    public CMSItemCreateException(String message)
    {
        super(message);
    }


    public CMSItemCreateException(Throwable cause)
    {
        super(cause);
    }
}
