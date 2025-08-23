package de.hybris.platform.jalo.type;

import de.hybris.platform.jalo.JaloBusinessException;

public abstract class JaloTypeException extends JaloBusinessException
{
    public JaloTypeException(Throwable nested, int vendorCode)
    {
        super(nested, vendorCode);
    }


    public JaloTypeException(String msg, int vendorCode)
    {
        super(msg, vendorCode);
    }
}
