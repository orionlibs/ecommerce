package de.hybris.platform.warehousing.atp.strategy;

import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import java.util.Collection;

public interface PickupWarehouseSelectionStrategy
{
    Collection<WarehouseModel> getWarehouses(PointOfServiceModel paramPointOfServiceModel);
}
