package de.hybris.platform.commercewebservices.core.product.data;

import java.io.Serializable;
import java.util.List;

public class ProductFutureStocksDataList implements Serializable
{
    private static final long serialVersionUID = 1L;
    private List<ProductFutureStocksData> productFutureStocks;


    public void setProductFutureStocks(List<ProductFutureStocksData> productFutureStocks)
    {
        this.productFutureStocks = productFutureStocks;
    }


    public List<ProductFutureStocksData> getProductFutureStocks()
    {
        return this.productFutureStocks;
    }
}
