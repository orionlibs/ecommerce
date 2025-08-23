package de.hybris.platform.commercefacades.storelocator.data;

import de.hybris.platform.commercefacades.product.data.StockData;

public class PointOfServiceStockData extends PointOfServiceData
{
    private StockData stockData;


    public void setStockData(StockData stockData)
    {
        this.stockData = stockData;
    }


    public StockData getStockData()
    {
        return this.stockData;
    }
}
