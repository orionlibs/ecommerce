package de.hybris.platform.commercewebservicescommons.dto.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value = "Stock", description = "Representation of a Stock")
public class StockWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "stockLevelStatus", value = "Status of stock level", example = "inStock")
    private String stockLevelStatus;
    @ApiModelProperty(name = "stockLevel", value = "Stock level expressed as number", example = "25")
    private Long stockLevel;
    @ApiModelProperty(name = "isValueRounded", value = "Indicate whether Stock level value is rounded", example = "false")
    private Boolean isValueRounded;


    public void setStockLevelStatus(String stockLevelStatus)
    {
        this.stockLevelStatus = stockLevelStatus;
    }


    public String getStockLevelStatus()
    {
        return this.stockLevelStatus;
    }


    public void setStockLevel(Long stockLevel)
    {
        this.stockLevel = stockLevel;
    }


    public Long getStockLevel()
    {
        return this.stockLevel;
    }


    public void setIsValueRounded(Boolean isValueRounded)
    {
        this.isValueRounded = isValueRounded;
    }


    public Boolean getIsValueRounded()
    {
        return this.isValueRounded;
    }
}
