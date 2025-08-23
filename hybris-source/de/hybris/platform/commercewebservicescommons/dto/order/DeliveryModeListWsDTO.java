package de.hybris.platform.commercewebservicescommons.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "DeliveryModeList", description = "Representation of a Delivery mode list")
public class DeliveryModeListWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "deliveryModes", value = "List of delivery modes")
    private List<DeliveryModeWsDTO> deliveryModes;


    public void setDeliveryModes(List<DeliveryModeWsDTO> deliveryModes)
    {
        this.deliveryModes = deliveryModes;
    }


    public List<DeliveryModeWsDTO> getDeliveryModes()
    {
        return this.deliveryModes;
    }
}
