package de.hybris.platform.jalo.order;

import de.hybris.platform.jalo.JaloSystemException;

public class JaloNotYetCalculatedException extends JaloSystemException
{
    public JaloNotYetCalculatedException(Exception exception, String message, int vendorCode)
    {
        super(exception, message, vendorCode);
    }
}
