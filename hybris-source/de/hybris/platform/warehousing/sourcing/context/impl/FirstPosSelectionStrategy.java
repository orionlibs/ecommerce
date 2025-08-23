package de.hybris.platform.warehousing.sourcing.context.impl;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import de.hybris.platform.warehousing.sourcing.context.PosSelectionStrategy;
import java.util.NoSuchElementException;

public class FirstPosSelectionStrategy implements PosSelectionStrategy
{
    public PointOfServiceModel getPointOfService(AbstractOrderModel orderModel, WarehouseModel warehouse)
    {
        try
        {
            return warehouse.getPointsOfService().iterator().next();
        }
        catch(NoSuchElementException exception)
        {
            return null;
        }
    }
}
