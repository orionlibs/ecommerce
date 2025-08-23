package de.hybris.platform.commercefacades.order.data;

import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;

public class PickupOrderEntryGroupData extends OrderEntryGroupData
{
    private PointOfServiceData deliveryPointOfService;
    private Double distance;


    public void setDeliveryPointOfService(PointOfServiceData deliveryPointOfService)
    {
        this.deliveryPointOfService = deliveryPointOfService;
    }


    public PointOfServiceData getDeliveryPointOfService()
    {
        return this.deliveryPointOfService;
    }


    public void setDistance(Double distance)
    {
        this.distance = distance;
    }


    public Double getDistance()
    {
        return this.distance;
    }
}
