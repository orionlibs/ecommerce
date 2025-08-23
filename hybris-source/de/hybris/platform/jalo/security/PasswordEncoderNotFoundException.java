package de.hybris.platform.jalo.security;

import de.hybris.platform.jalo.JaloSystemException;

public class PasswordEncoderNotFoundException extends JaloSystemException
{
    public PasswordEncoderNotFoundException(String message, int vendorCode)
    {
        super(null, message, vendorCode);
    }
}
