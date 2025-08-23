package de.hybris.platform.commercewebservicescommons.dto.order;

import de.hybris.platform.commercewebservicescommons.dto.user.AddressWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "DeliveryOrderEntryGroup", description = "Representation of a Delivery Order Entry Group")
public class DeliveryOrderEntryGroupWsDTO extends OrderEntryGroupWsDTO
{
    @ApiModelProperty(name = "deliveryAddress", value = "Delivery address for order entry group")
    private AddressWsDTO deliveryAddress;


    public void setDeliveryAddress(AddressWsDTO deliveryAddress)
    {
        this.deliveryAddress = deliveryAddress;
    }


    public AddressWsDTO getDeliveryAddress()
    {
        return this.deliveryAddress;
    }
}
