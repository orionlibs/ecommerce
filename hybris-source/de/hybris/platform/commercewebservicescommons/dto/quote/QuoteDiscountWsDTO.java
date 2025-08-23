package de.hybris.platform.commercewebservicescommons.dto.quote;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value = "QuoteDiscount", description = "Discount applied to the quote")
public class QuoteDiscountWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "discountType", value = "Type of the discount - PERCENT for discount by percentage, ABSOLUTE for discount by amount, TARGET for discount by adjustment of the total value", required = false, example = "PERCENT")
    private String discountType;
    @ApiModelProperty(name = "discountRate", value = "Value of the discount", required = false, example = "10")
    private Double discountRate;


    public void setDiscountType(String discountType)
    {
        this.discountType = discountType;
    }


    public String getDiscountType()
    {
        return this.discountType;
    }


    public void setDiscountRate(Double discountRate)
    {
        this.discountRate = discountRate;
    }


    public Double getDiscountRate()
    {
        return this.discountRate;
    }
}
