package de.hybris.platform.warehousing.sourcing.context.populator;

import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.warehousing.data.sourcing.SourcingLocation;

public interface SourcingLocationPopulator
{
    void populate(WarehouseModel paramWarehouseModel, SourcingLocation paramSourcingLocation);
}
