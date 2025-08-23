package de.hybris.platform.ordersplitting.strategy.impl;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.ordersplitting.strategy.AbstractSplittingStrategy;
import de.hybris.platform.stock.StockService;
import org.springframework.beans.factory.annotation.Required;

public class SplitByAvailableCount extends AbstractSplittingStrategy
{
    private StockService stockService;


    @Required
    public void setStockService(StockService stockService)
    {
        this.stockService = stockService;
    }


    public Object getGroupingObject(AbstractOrderEntryModel orderEntry)
    {
        int res = 0;
        for(StockLevelModel stockLevel : this.stockService.getAllStockLevels(orderEntry.getProduct()))
        {
            res += stockLevel.getAvailable();
        }
        return Boolean.valueOf((res >= orderEntry.getQuantity().intValue()));
    }


    public void afterSplitting(Object groupingObject, ConsignmentModel createdOne)
    {
    }
}
