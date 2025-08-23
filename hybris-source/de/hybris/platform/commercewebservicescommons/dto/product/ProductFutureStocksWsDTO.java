package de.hybris.platform.commercewebservicescommons.dto.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "ProductFutureStocks", description = "Representation of a Product Future Stocks")
public class ProductFutureStocksWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "productCode", value = "Product identifier", example = "3318057")
    private String productCode;
    @ApiModelProperty(name = "futureStocks", value = "List of future stocks")
    private List<FutureStockWsDTO> futureStocks;


    public void setProductCode(String productCode)
    {
        this.productCode = productCode;
    }


    public String getProductCode()
    {
        return this.productCode;
    }


    public void setFutureStocks(List<FutureStockWsDTO> futureStocks)
    {
        this.futureStocks = futureStocks;
    }


    public List<FutureStockWsDTO> getFutureStocks()
    {
        return this.futureStocks;
    }
}
