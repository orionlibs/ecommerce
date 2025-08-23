package de.hybris.platform.warehousing.returns.strategy;

import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.returns.model.ReturnRequestModel;

public interface RestockWarehouseSelectionStrategy
{
    WarehouseModel performStrategy(ReturnRequestModel paramReturnRequestModel);
}
