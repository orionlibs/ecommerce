package de.hybris.platform.persistence.security;

import de.hybris.platform.jalo.JaloBusinessException;

public class EJBSecurityException extends JaloBusinessException
{
    public EJBSecurityException(Throwable throwable, String message, int vendorCode)
    {
        super(throwable, message, vendorCode);
    }
}
