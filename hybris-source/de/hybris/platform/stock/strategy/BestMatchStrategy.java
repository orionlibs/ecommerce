package de.hybris.platform.stock.strategy;

import de.hybris.platform.ordersplitting.model.WarehouseModel;
import java.util.Date;
import java.util.Map;

public interface BestMatchStrategy
{
    WarehouseModel getBestMatchOfQuantity(Map<WarehouseModel, Integer> paramMap);


    WarehouseModel getBestMatchOfAvailability(Map<WarehouseModel, Date> paramMap);
}
