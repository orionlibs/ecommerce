package de.hybris.platform.jalo.order.delivery;

import de.hybris.platform.jalo.JaloBusinessException;

public class JaloDeliveryModeException extends JaloBusinessException
{
    public JaloDeliveryModeException(String message, int vendorCode)
    {
        super(message, vendorCode);
    }


    public JaloDeliveryModeException(Throwable nested, int vendorCode)
    {
        super(nested, vendorCode);
    }
}
