package de.hybris.platform.warehousing.orderentry.service;

import de.hybris.platform.core.model.order.OrderEntryModel;

public interface OrderEntryQuantityService
{
    Long getQuantityShipped(OrderEntryModel paramOrderEntryModel);


    Long getQuantityCancelled(OrderEntryModel paramOrderEntryModel);


    Long getQuantityAllocated(OrderEntryModel paramOrderEntryModel);


    Long getQuantityUnallocated(OrderEntryModel paramOrderEntryModel);


    Long getQuantityPending(OrderEntryModel paramOrderEntryModel);


    Long getQuantityReturned(OrderEntryModel paramOrderEntryModel);


    Long getQuantityDeclined(OrderEntryModel paramOrderEntryModel);
}
