package de.hybris.platform.warehousing.sourcing.context.populator.impl;

import com.google.common.base.Preconditions;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.warehousing.data.sourcing.SourcingLocation;
import de.hybris.platform.warehousing.sourcing.context.populator.SourcingLocationPopulator;

public class PrioritySourcingLocationPopulator implements SourcingLocationPopulator
{
    public void populate(WarehouseModel source, SourcingLocation target)
    {
        Preconditions.checkArgument((source != null), "warehouse model (source) cannot be null.");
        Preconditions.checkArgument((target != null), "Sourcing location (target) cannot be null.");
        target.setPriority(source.getPriority());
    }
}
