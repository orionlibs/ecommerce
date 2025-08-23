package de.hybris.platform.warehousing.inventoryevent.service;

import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.warehousing.data.allocation.DeclineEntry;
import de.hybris.platform.warehousing.model.AllocationEventModel;
import de.hybris.platform.warehousing.model.CancellationEventModel;
import de.hybris.platform.warehousing.model.IncreaseEventModel;
import de.hybris.platform.warehousing.model.ShrinkageEventModel;
import de.hybris.platform.warehousing.model.WastageEventModel;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

public interface InventoryEventService extends Serializable
{
    Collection<AllocationEventModel> createAllocationEvents(ConsignmentModel paramConsignmentModel);


    Collection<AllocationEventModel> createAllocationEventsForConsignmentEntry(ConsignmentEntryModel paramConsignmentEntryModel);


    ShrinkageEventModel createShrinkageEvent(ShrinkageEventModel paramShrinkageEventModel);


    WastageEventModel createWastageEvent(WastageEventModel paramWastageEventModel);


    Collection<CancellationEventModel> createCancellationEvents(CancellationEventModel paramCancellationEventModel);


    Collection<AllocationEventModel> getAllocationEventsForConsignmentEntry(ConsignmentEntryModel paramConsignmentEntryModel);


    Collection<AllocationEventModel> getAllocationEventsForOrderEntry(OrderEntryModel paramOrderEntryModel);


    <T extends de.hybris.platform.warehousing.model.InventoryEventModel> Collection<T> getInventoryEventsForStockLevel(StockLevelModel paramStockLevelModel, Class<T> paramClass);


    IncreaseEventModel createIncreaseEvent(IncreaseEventModel paramIncreaseEventModel);


    void reallocateAllocationEvent(DeclineEntry paramDeclineEntry, Long paramLong);


    Map<AllocationEventModel, Long> getAllocationEventsForReallocation(Collection<AllocationEventModel> paramCollection, Long paramLong);


    Collection<AllocationEventModel> getAllocationEventsForConsignment(ConsignmentModel paramConsignmentModel);
}
