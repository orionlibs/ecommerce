package de.hybris.platform.commercewebservices.core.product.data;

import de.hybris.platform.commercefacades.product.data.FutureStockData;
import java.io.Serializable;
import java.util.List;

public class ProductFutureStocksData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String productCode;
    private List<FutureStockData> futureStocks;


    public void setProductCode(String productCode)
    {
        this.productCode = productCode;
    }


    public String getProductCode()
    {
        return this.productCode;
    }


    public void setFutureStocks(List<FutureStockData> futureStocks)
    {
        this.futureStocks = futureStocks;
    }


    public List<FutureStockData> getFutureStocks()
    {
        return this.futureStocks;
    }
}
