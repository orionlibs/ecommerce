package de.hybris.platform.commercefacades.order.data;

import java.io.Serializable;
import java.util.List;

public class PaymentModeDataList implements Serializable
{
    private static final long serialVersionUID = 1L;
    private List<PaymentModeData> paymentModes;


    public void setPaymentModes(List<PaymentModeData> paymentModes)
    {
        this.paymentModes = paymentModes;
    }


    public List<PaymentModeData> getPaymentModes()
    {
        return this.paymentModes;
    }
}
