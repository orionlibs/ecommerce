package de.hybris.platform.warehousing.sourcing.context;

import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.warehousing.data.sourcing.SourcingContext;
import de.hybris.platform.warehousing.sourcing.context.grouping.OrderEntryGroup;
import java.util.Collection;

public interface SourcingContextFactory
{
    Collection<SourcingContext> create(Collection<OrderEntryGroup> paramCollection, Collection<WarehouseModel> paramCollection1);
}
