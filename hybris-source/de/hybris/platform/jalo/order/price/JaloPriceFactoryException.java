package de.hybris.platform.jalo.order.price;

import de.hybris.platform.jalo.JaloBusinessException;

public class JaloPriceFactoryException extends JaloBusinessException
{
    public JaloPriceFactoryException(Throwable nested, int aVendorCode)
    {
        super(nested, aVendorCode);
    }


    public JaloPriceFactoryException(String message, int vendorCode)
    {
        super(message, vendorCode);
    }
}
