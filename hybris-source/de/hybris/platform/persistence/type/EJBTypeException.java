package de.hybris.platform.persistence.type;

import de.hybris.platform.jalo.JaloBusinessException;

public abstract class EJBTypeException extends JaloBusinessException
{
    public EJBTypeException(Exception nested, String message, int vendorCode)
    {
        super(nested, message, vendorCode);
    }
}
