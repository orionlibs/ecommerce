package de.hybris.platform.ordersplitting;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import java.util.Collection;
import java.util.List;

public interface WarehouseService
{
    List<WarehouseModel> getWarehousesWithProductsInStock(AbstractOrderEntryModel paramAbstractOrderEntryModel);


    List<WarehouseModel> getWarehouses(Collection<? extends AbstractOrderEntryModel> paramCollection);


    WarehouseModel getWarehouseForCode(String paramString);


    List<WarehouseModel> getDefWarehouse();
}
