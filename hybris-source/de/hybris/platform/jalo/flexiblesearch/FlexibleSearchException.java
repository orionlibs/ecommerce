package de.hybris.platform.jalo.flexiblesearch;

import de.hybris.platform.jalo.JaloSystemException;

public class FlexibleSearchException extends JaloSystemException
{
    public FlexibleSearchException(String message)
    {
        super(null, message, 0);
    }


    public FlexibleSearchException(Throwable throwable, String message)
    {
        super(throwable, message, 0);
    }


    public FlexibleSearchException(Throwable throwable, String message, int vendorCode)
    {
        super(throwable, message, vendorCode);
    }
}
