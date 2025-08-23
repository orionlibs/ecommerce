package de.hybris.platform.warehousing.warehouse.filter;

import de.hybris.platform.ordersplitting.model.WarehouseModel;
import java.util.Set;

public interface WarehousesFilter
{
    Set<WarehouseModel> applyFilter(Set<WarehouseModel> paramSet);
}
