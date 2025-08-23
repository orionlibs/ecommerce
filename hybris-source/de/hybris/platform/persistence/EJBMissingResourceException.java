package de.hybris.platform.persistence;

import de.hybris.platform.jalo.JaloBusinessException;

public class EJBMissingResourceException extends JaloBusinessException
{
    public EJBMissingResourceException(Throwable throwable, String message, int vendorCode)
    {
        super(throwable, message, vendorCode);
    }
}
