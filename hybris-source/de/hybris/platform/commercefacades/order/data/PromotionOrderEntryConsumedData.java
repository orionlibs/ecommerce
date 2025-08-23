package de.hybris.platform.commercefacades.order.data;

import java.io.Serializable;

public class PromotionOrderEntryConsumedData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String code;
    private Double adjustedUnitPrice;
    private Integer orderEntryNumber;
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
