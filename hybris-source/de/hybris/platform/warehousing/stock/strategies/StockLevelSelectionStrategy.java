package de.hybris.platform.warehousing.stock.strategies;

import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.warehousing.model.AllocationEventModel;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

public interface StockLevelSelectionStrategy extends Serializable
{
    Map<StockLevelModel, Long> getStockLevelsForAllocation(Collection<StockLevelModel> paramCollection, Long paramLong);


    Map<StockLevelModel, Long> getStockLevelsForCancellation(Collection<AllocationEventModel> paramCollection, Long paramLong);
}
