package de.hybris.platform.commercewebservicescommons.dto.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "ProductFutureStocksList", description = "Representation of a Product Future Stocks List")
public class ProductFutureStocksListWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "productFutureStocks", value = "List of product future stocks")
    private List<ProductFutureStocksWsDTO> productFutureStocks;


    public void setProductFutureStocks(List<ProductFutureStocksWsDTO> productFutureStocks)
    {
        this.productFutureStocks = productFutureStocks;
    }


    public List<ProductFutureStocksWsDTO> getProductFutureStocks()
    {
        return this.productFutureStocks;
    }
}
