package de.hybris.platform.jalo.security;

import de.hybris.platform.jalo.JaloBusinessException;

public class JaloSecurityException extends JaloBusinessException
{
    public JaloSecurityException(String message, int vendorCode)
    {
        super(message, vendorCode);
    }


    public JaloSecurityException(Exception e, int vendorCode)
    {
        super(e, vendorCode);
    }
}
