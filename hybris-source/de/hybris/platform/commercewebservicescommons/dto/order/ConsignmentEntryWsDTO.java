package de.hybris.platform.commercewebservicescommons.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "ConsignmentEntry", description = "Representation of a Consignment Entry")
public class ConsignmentEntryWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "orderEntry", value = "Order entry of Consignment entry")
    private OrderEntryWsDTO orderEntry;
    @ApiModelProperty(name = "quantity", value = "Quantity value of Consignment entry")
    private Long quantity;
    @ApiModelProperty(name = "shippedQuantity", value = "Shipped quantity")
    private Long shippedQuantity;
    @ApiModelProperty(name = "otherVariants")
    private List<ConsignmentEntryWsDTO> otherVariants;


    public void setOrderEntry(OrderEntryWsDTO orderEntry)
    {
        this.orderEntry = orderEntry;
    }


    public OrderEntryWsDTO getOrderEntry()
    {
        return this.orderEntry;
    }


    public void setQuantity(Long quantity)
    {
        this.quantity = quantity;
    }


    public Long getQuantity()
    {
        return this.quantity;
    }


    public void setShippedQuantity(Long shippedQuantity)
    {
        this.shippedQuantity = shippedQuantity;
    }


    public Long getShippedQuantity()
    {
        return this.shippedQuantity;
    }


    public void setOtherVariants(List<ConsignmentEntryWsDTO> otherVariants)
    {
        this.otherVariants = otherVariants;
    }


    public List<ConsignmentEntryWsDTO> getOtherVariants()
    {
        return this.otherVariants;
    }
}
