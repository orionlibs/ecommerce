package de.hybris.platform.persistence.order.price;

import de.hybris.platform.jalo.JaloBusinessException;

public class EJBPriceFactoryException extends JaloBusinessException
{
    public EJBPriceFactoryException(Throwable throwable, String message, int vendorCode)
    {
        super(throwable, message, vendorCode);
    }
}
