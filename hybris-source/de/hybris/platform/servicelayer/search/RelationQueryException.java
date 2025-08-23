package de.hybris.platform.servicelayer.search;

import de.hybris.platform.jalo.JaloSystemException;

public class RelationQueryException extends JaloSystemException
{
    public RelationQueryException(String message)
    {
        super(null, message, 0);
    }


    public RelationQueryException(Throwable throwable, String message)
    {
        super(throwable, message, 0);
    }


    public RelationQueryException(Throwable throwable, String message, int vendorCode)
    {
        super(throwable, message, vendorCode);
    }
}
