package de.hybris.platform.commercewebservicescommons.dto.order;

import de.hybris.platform.commercewebservicescommons.dto.store.PointOfServiceWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "PickupOrderEntryGroup", description = "Representation of a Pickup Order Entry Group")
public class PickupOrderEntryGroupWsDTO extends OrderEntryGroupWsDTO
{
    @ApiModelProperty(name = "deliveryPointOfService", value = "Delivery point of service")
    private PointOfServiceWsDTO deliveryPointOfService;
    @ApiModelProperty(name = "distance", value = "Distance calculated to pickup place")
    private Double distance;


    public void setDeliveryPointOfService(PointOfServiceWsDTO deliveryPointOfService)
    {
        this.deliveryPointOfService = deliveryPointOfService;
    }


    public PointOfServiceWsDTO getDeliveryPointOfService()
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
