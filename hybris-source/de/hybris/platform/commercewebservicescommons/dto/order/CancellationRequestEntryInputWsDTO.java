package de.hybris.platform.commercewebservicescommons.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value = "CancellationRequestEntryInput", description = "Representation of a cancellation request entry input for an order")
public class CancellationRequestEntryInputWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "orderEntryNumber", value = "Order entry number of the cancelled product", required = true, example = "1")
    private Integer orderEntryNumber;
    @ApiModelProperty(name = "quantity", value = "Quantity of the product which belongs to the order entry and is requested to be cancelled", required = true, example = "5")
    private Long quantity;


    public void setOrderEntryNumber(Integer orderEntryNumber)
    {
        this.orderEntryNumber = orderEntryNumber;
    }


    public Integer getOrderEntryNumber()
    {
        return this.orderEntryNumber;
    }


    public void setQuantity(Long quantity)
    {
        this.quantity = quantity;
    }


    public Long getQuantity()
    {
        return this.quantity;
    }
}
