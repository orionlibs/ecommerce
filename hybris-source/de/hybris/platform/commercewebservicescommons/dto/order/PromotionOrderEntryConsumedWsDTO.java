package de.hybris.platform.commercewebservicescommons.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value = "PromotionOrderEntryConsumed", description = "Representation of a Promotion order entry consumed")
public class PromotionOrderEntryConsumedWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "code", value = "Order entry code")
    private String code;
    @ApiModelProperty(name = "adjustedUnitPrice", value = "Adjusted unit price for promotion order entry")
    private Double adjustedUnitPrice;
    @ApiModelProperty(name = "orderEntryNumber", value = "Order entry number")
    private Integer orderEntryNumber;
    @ApiModelProperty(name = "quantity", value = "Quantity of promotion order entry")
    private Long quantity;


    public void setCode(String code)
    {
        this.code = code;
    }


    public String getCode()
    {
        return this.code;
    }


    public void setAdjustedUnitPrice(Double adjustedUnitPrice)
    {
        this.adjustedUnitPrice = adjustedUnitPrice;
    }


    public Double getAdjustedUnitPrice()
    {
        return this.adjustedUnitPrice;
    }


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
