package de.hybris.platform.warehousing.sourcing.context;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import java.io.Serializable;

public interface PosSelectionStrategy extends Serializable
{
    PointOfServiceModel getPointOfService(AbstractOrderModel paramAbstractOrderModel, WarehouseModel paramWarehouseModel);
}
