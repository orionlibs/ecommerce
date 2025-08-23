package de.hybris.platform.commercewebservicescommons.dto.product;

import java.io.Serializable;
import java.util.List;

public class DeliveryOptionListWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    private List<DeliveryOptionWsDTO> deliveryOption;


    public void setDeliveryOption(List<DeliveryOptionWsDTO> deliveryOption)
    {
        this.deliveryOption = deliveryOption;
    }


    public List<DeliveryOptionWsDTO> getDeliveryOption()
    {
        return this.deliveryOption;
    }
}
