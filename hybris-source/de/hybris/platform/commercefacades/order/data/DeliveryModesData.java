package de.hybris.platform.commercefacades.order.data;

import java.io.Serializable;
import java.util.List;

public class DeliveryModesData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private List<? extends DeliveryModeData> deliveryModes;


    public void setDeliveryModes(List<? extends DeliveryModeData> deliveryModes)
    {
        this.deliveryModes = deliveryModes;
    }


    public List<? extends DeliveryModeData> getDeliveryModes()
    {
        return this.deliveryModes;
    }
}
