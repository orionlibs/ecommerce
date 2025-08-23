package de.hybris.platform.persistence;

import de.hybris.platform.jalo.JaloSystemException;

public class EJBInternalException extends JaloSystemException
{
    public EJBInternalException(Exception exception, String message, int vendorCode)
    {
        super(exception, message, vendorCode);
    }


    public EJBInternalException(Exception exception)
    {
        super(exception, exception.getMessage(), 0);
    }
}
