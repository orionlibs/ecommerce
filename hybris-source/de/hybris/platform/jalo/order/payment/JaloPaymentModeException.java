package de.hybris.platform.jalo.order.payment;

import de.hybris.platform.jalo.JaloBusinessException;

public class JaloPaymentModeException extends JaloBusinessException
{
    public JaloPaymentModeException(String message, int vendorCode)
    {
        super(message, vendorCode);
    }


    public JaloPaymentModeException(Throwable nested, int vendorCode)
    {
        super(nested, vendorCode);
    }
}
