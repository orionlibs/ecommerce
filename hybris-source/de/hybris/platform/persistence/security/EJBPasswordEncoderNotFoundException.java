package de.hybris.platform.persistence.security;

import de.hybris.platform.jalo.JaloBusinessException;

public class EJBPasswordEncoderNotFoundException extends JaloBusinessException
{
    public EJBPasswordEncoderNotFoundException(Throwable throwable, String message, int vendorCode)
    {
        super(throwable, message, vendorCode);
    }
}
