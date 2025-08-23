package de.hybris.platform.stock.strategy.impl;

import de.hybris.platform.basecommerce.enums.InStockStatus;
import de.hybris.platform.basecommerce.enums.StockLevelStatus;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.stock.strategy.StockLevelStatusStrategy;
import java.util.Collection;

public class DefaultStockLevelStatusStrategy implements StockLevelStatusStrategy
{
    public StockLevelStatus checkStatus(StockLevelModel stockLevel)
    {
        if(stockLevel == null)
        {
            return StockLevelStatus.OUTOFSTOCK;
        }
        if(InStockStatus.FORCEINSTOCK.equals(stockLevel.getInStockStatus()))
        {
            return StockLevelStatus.INSTOCK;
        }
        int result = stockLevel.getAvailable() - stockLevel.getReserved() + stockLevel.getOverSelling();
        if(result > 0)
        {
            return StockLevelStatus.INSTOCK;
        }
        return StockLevelStatus.OUTOFSTOCK;
    }


    public StockLevelStatus checkStatus(Collection<StockLevelModel> stockLevels)
    {
        StockLevelStatus resultStatus = StockLevelStatus.OUTOFSTOCK;
        for(StockLevelModel level : stockLevels)
        {
            resultStatus = checkStatus(level);
            if(StockLevelStatus.INSTOCK.equals(resultStatus))
            {
                break;
            }
        }
        return resultStatus;
    }
}
