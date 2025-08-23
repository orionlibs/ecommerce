package de.hybris.platform.commercewebservicescommons.dto.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value = "PriceRange", description = "Representation of a Price Range")
public class PriceRangeWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "maxPrice", value = "Maximum value of the Price Range")
    private PriceWsDTO maxPrice;
    @ApiModelProperty(name = "minPrice", value = "Minium value of the Price Range")
    private PriceWsDTO minPrice;


    public void setMaxPrice(PriceWsDTO maxPrice)
    {
        this.maxPrice = maxPrice;
    }


    public PriceWsDTO getMaxPrice()
    {
        return this.maxPrice;
    }


    public void setMinPrice(PriceWsDTO minPrice)
    {
        this.minPrice = minPrice;
    }


    public PriceWsDTO getMinPrice()
    {
        return this.minPrice;
    }
}
