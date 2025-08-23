package de.hybris.platform.commercewebservicescommons.dto.store;

import java.io.Serializable;
import java.util.List;

public class ShippingCarrierListWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    private List<ShippingCarrierWsDTO> shippingCarriers;


    public void setShippingCarriers(List<ShippingCarrierWsDTO> shippingCarriers)
    {
        this.shippingCarriers = shippingCarriers;
    }


    public List<ShippingCarrierWsDTO> getShippingCarriers()
    {
        return this.shippingCarriers;
    }
}
