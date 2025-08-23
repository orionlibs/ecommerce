package de.hybris.platform.jalo.security;

import de.hybris.platform.jalo.JaloSystemException;

public class CannotDecodePasswordException extends JaloSystemException
{
    public CannotDecodePasswordException(String message, int vendorCode)
    {
        super(null, message, vendorCode);
    }
}
