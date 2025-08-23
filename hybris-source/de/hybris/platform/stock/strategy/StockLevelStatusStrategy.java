package de.hybris.platform.stock.strategy;

import de.hybris.platform.basecommerce.enums.StockLevelStatus;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import java.util.Collection;

public interface StockLevelStatusStrategy
{
    StockLevelStatus checkStatus(StockLevelModel paramStockLevelModel);


    StockLevelStatus checkStatus(Collection<StockLevelModel> paramCollection);
}
