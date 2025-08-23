package de.hybris.platform.commercewebservicescommons.dto.store;

import de.hybris.platform.commercewebservicescommons.dto.product.StockWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "PointOfServiceStock", description = "Representation of a Point Of Service Stock")
public class PointOfServiceStockWsDTO extends PointOfServiceWsDTO
{
    @ApiModelProperty(name = "stockInfo", value = "Stock information about point of service")
    private StockWsDTO stockInfo;


    public void setStockInfo(StockWsDTO stockInfo)
    {
        this.stockInfo = stockInfo;
    }


    public StockWsDTO getStockInfo()
    {
        return this.stockInfo;
    }
}
