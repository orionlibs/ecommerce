package de.hybris.platform.commercewebservicescommons.dto.order;

import de.hybris.platform.commercewebservicescommons.dto.product.PriceWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value = "DeliveryMode", description = "Representation of a Delivery mode")
public class DeliveryModeWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "code", value = "Code of the delivery mode")
    private String code;
    @ApiModelProperty(name = "name", value = "Name of the delivery mode")
    private String name;
    @ApiModelProperty(name = "description", value = "Description of the delivery mode")
    private String description;
    @ApiModelProperty(name = "deliveryCost", value = "Cost of the delivery")
    private PriceWsDTO deliveryCost;


    public void setCode(String code)
    {
        this.code = code;
    }


    public String getCode()
    {
        return this.code;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return this.name;
    }


    public void setDescription(String description)
    {
        this.description = description;
    }


    public String getDescription()
    {
        return this.description;
    }


    public void setDeliveryCost(PriceWsDTO deliveryCost)
    {
        this.deliveryCost = deliveryCost;
    }


    public PriceWsDTO getDeliveryCost()
    {
        return this.deliveryCost;
    }
}
