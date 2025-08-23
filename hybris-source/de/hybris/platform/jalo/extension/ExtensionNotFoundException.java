package de.hybris.platform.jalo.extension;

import de.hybris.platform.jalo.JaloSystemException;

public class ExtensionNotFoundException extends JaloSystemException
{
    public ExtensionNotFoundException(String message, int vendorCode)
    {
        super(null, message, vendorCode);
    }
}
