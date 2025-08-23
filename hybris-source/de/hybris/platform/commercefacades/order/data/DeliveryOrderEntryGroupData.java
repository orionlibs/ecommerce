package de.hybris.platform.commercefacades.order.data;

import de.hybris.platform.commercefacades.user.data.AddressData;

public class DeliveryOrderEntryGroupData extends OrderEntryGroupData
{
    private AddressData deliveryAddress;


    public void setDeliveryAddress(AddressData deliveryAddress)
    {
        this.deliveryAddress = deliveryAddress;
    }


    public AddressData getDeliveryAddress()
    {
        return this.deliveryAddress;
    }
}
