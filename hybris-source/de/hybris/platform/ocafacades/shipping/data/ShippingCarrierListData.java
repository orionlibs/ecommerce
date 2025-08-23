package de.hybris.platform.ocafacades.shipping.data;

import java.io.Serializable;
import java.util.List;

public class ShippingCarrierListData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private List<ShippingCarrierData> shippingCarriers;


    public void setShippingCarriers(List<ShippingCarrierData> shippingCarriers)
    {
        this.shippingCarriers = shippingCarriers;
    }


    public List<ShippingCarrierData> getShippingCarriers()
    {
        return this.shippingCarriers;
    }
}
