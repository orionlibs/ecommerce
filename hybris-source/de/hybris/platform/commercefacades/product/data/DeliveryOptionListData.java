package de.hybris.platform.commercefacades.product.data;

import java.io.Serializable;
import java.util.List;

public class DeliveryOptionListData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private List<DeliveryOptionData> deliveryOption;


    public void setDeliveryOption(List<DeliveryOptionData> deliveryOption)
    {
        this.deliveryOption = deliveryOption;
    }


    public List<DeliveryOptionData> getDeliveryOption()
    {
        return this.deliveryOption;
    }
}
