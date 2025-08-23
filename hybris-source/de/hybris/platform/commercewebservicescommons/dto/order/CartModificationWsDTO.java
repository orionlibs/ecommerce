package de.hybris.platform.commercewebservicescommons.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value = "CartModification", description = "Representation of a Cart modification")
public class CartModificationWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "statusCode", value = "Status code of cart modification")
    private String statusCode;
    @ApiModelProperty(name = "quantityAdded", value = "Quantity added with cart modification")
    private Long quantityAdded;
    @ApiModelProperty(name = "quantity", value = "Final quantity after cart modification")
    private Long quantity;
    @ApiModelProperty(name = "entry", value = "Order entry")
    private OrderEntryWsDTO entry;
    @ApiModelProperty(name = "deliveryModeChanged", value = "Delivery mode changed")
    private Boolean deliveryModeChanged;
    @ApiModelProperty(name = "statusMessage", value = "Status message")
    private String statusMessage;


    public void setStatusCode(String statusCode)
    {
        this.statusCode = statusCode;
    }


    public String getStatusCode()
    {
        return this.statusCode;
    }


    public void setQuantityAdded(Long quantityAdded)
    {
        this.quantityAdded = quantityAdded;
    }


    public Long getQuantityAdded()
    {
        return this.quantityAdded;
    }


    public void setQuantity(Long quantity)
    {
        this.quantity = quantity;
    }


    public Long getQuantity()
    {
        return this.quantity;
    }


    public void setEntry(OrderEntryWsDTO entry)
    {
        this.entry = entry;
    }


    public OrderEntryWsDTO getEntry()
    {
        return this.entry;
    }


    public void setDeliveryModeChanged(Boolean deliveryModeChanged)
    {
        this.deliveryModeChanged = deliveryModeChanged;
    }


    public Boolean getDeliveryModeChanged()
    {
        return this.deliveryModeChanged;
    }


    public void setStatusMessage(String statusMessage)
    {
        this.statusMessage = statusMessage;
    }


    public String getStatusMessage()
    {
        return this.statusMessage;
    }
}
