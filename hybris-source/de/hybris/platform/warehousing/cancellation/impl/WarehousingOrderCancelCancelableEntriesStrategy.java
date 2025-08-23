package de.hybris.platform.warehousing.cancellation.impl;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.ordercancel.OrderCancelCancelableEntriesStrategy;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class WarehousingOrderCancelCancelableEntriesStrategy implements OrderCancelCancelableEntriesStrategy
{
    public Map<AbstractOrderEntryModel, Long> getAllCancelableEntries(OrderModel order, PrincipalModel requestor)
    {
        Map<AbstractOrderEntryModel, Long> cancellableEntries = new HashMap<>();
        order.getEntries().stream().filter(entry -> (((Long)Optional.<Long>ofNullable(((OrderEntryModel)entry).getQuantityPending()).orElse(Long.valueOf(-1L))).longValue() > 0L))
                        .forEach(entry -> cancellableEntries.put(entry, ((OrderEntryModel)entry).getQuantityPending()));
        return cancellableEntries;
    }
}
