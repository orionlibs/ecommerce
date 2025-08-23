package de.hybris.platform.commercewebservicescommons.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value = "ReturnRequestEntryInput", description = "Representation of a return request entry input for an order")
public class ReturnRequestEntryInputWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "orderEntryNumber", value = "Order entry number of the returned product", required = true, example = "1")
    private Integer orderEntryNumber;
    @ApiModelProperty(name = "quantity", value = "Quantity of the product which belongs to the order entry and is requested to be returned", required = true, example = "5")
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
