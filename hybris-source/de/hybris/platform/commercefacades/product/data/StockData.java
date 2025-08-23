package de.hybris.platform.commercefacades.product.data;

import de.hybris.platform.basecommerce.enums.StockLevelStatus;
import java.io.Serializable;

public class StockData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private StockLevelStatus stockLevelStatus;
    private Long stockLevel;
    private Integer stockThreshold;


    public void setStockLevelStatus(StockLevelStatus stockLevelStatus)
    {
        this.stockLevelStatus = stockLevelStatus;
    }


    public StockLevelStatus getStockLevelStatus()
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


    public void setStockThreshold(Integer stockThreshold)
    {
        this.stockThreshold = stockThreshold;
    }


    public Integer getStockThreshold()
    {
        return this.stockThreshold;
    }
}
