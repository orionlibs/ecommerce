package de.hybris.platform.warehousing.inventoryevent.dao;

import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.warehousing.model.AllocationEventModel;
import java.io.Serializable;
import java.util.Collection;

public interface InventoryEventDao extends Serializable
{
    Collection<AllocationEventModel> getAllocationEventsForConsignmentEntry(ConsignmentEntryModel paramConsignmentEntryModel);


    Collection<AllocationEventModel> getAllocationEventsForOrderEntry(OrderEntryModel paramOrderEntryModel);


    <T extends de.hybris.platform.warehousing.model.InventoryEventModel> Collection<T> getInventoryEventsForStockLevel(StockLevelModel paramStockLevelModel, Class<T> paramClass);


    Collection<AllocationEventModel> getAllocationEventsForConsignment(ConsignmentModel paramConsignmentModel);
}
