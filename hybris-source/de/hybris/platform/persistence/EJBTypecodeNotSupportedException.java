package de.hybris.platform.persistence;

import de.hybris.platform.jalo.JaloBusinessException;

public class EJBTypecodeNotSupportedException extends JaloBusinessException
{
    public EJBTypecodeNotSupportedException(Throwable throwable, String message, int vendorCode)
    {
        super(throwable, message, vendorCode);
    }
}
