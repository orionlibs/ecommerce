package de.hybris.platform.persistence;

import de.hybris.platform.jalo.JaloBusinessException;

public class EJBInvalidParameterException extends JaloBusinessException
{
    public EJBInvalidParameterException(Throwable throwable, String message, int vendorCode)
    {
        super(throwable, message, vendorCode);
    }


    public EJBInvalidParameterException(Throwable throwable)
    {
        super(throwable, throwable.getMessage(), 0);
    }
}
