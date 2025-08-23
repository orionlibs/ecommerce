package de.hybris.platform.ordercancel.impl;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.ordercancel.OrderCancelCancelableEntriesStrategy;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

public class DefaultOrderCancelCancelableEntriesStrategy implements OrderCancelCancelableEntriesStrategy
{
    private static final Logger LOG = Logger.getLogger(DefaultOrderCancelCancelableEntriesStrategy.class.getName());


    public Map<AbstractOrderEntryModel, Long> getAllCancelableEntries(OrderModel order, PrincipalModel requestor)
    {
        Map<AbstractOrderEntryModel, Long> uncancelableEntriesMap = collectUncancelableEntriesMap(order);
        return findCancellableEntries(order, uncancelableEntriesMap);
    }


    protected Map<AbstractOrderEntryModel, Long> findCancellableEntries(OrderModel order, Map<AbstractOrderEntryModel, Long> uncancelableEntriesMap)
    {
        Map<AbstractOrderEntryModel, Long> cancelableEntries = new HashMap<>();
        for(AbstractOrderEntryModel entry : order.getEntries())
        {
            long uncancelableEntryQuantity, totalEntryQuantity = entry.getQuantity().longValue();
            if(uncancelableEntriesMap.containsKey(entry))
            {
                uncancelableEntryQuantity = ((Long)uncancelableEntriesMap.get(entry)).longValue();
            }
            else
            {
                uncancelableEntryQuantity = 0L;
            }
            long cancelableQuantity = totalEntryQuantity - uncancelableEntryQuantity;
            if(cancelableQuantity > 0L)
            {
                cancelableEntries.put(entry, Long.valueOf(cancelableQuantity));
                continue;
            }
            if(cancelableQuantity < 0L)
            {
                LOG.error("Error while computing cancelableQuantity of order entry: result value < 0");
            }
        }
        return cancelableEntries;
    }


    protected Map<AbstractOrderEntryModel, Long> collectUncancelableEntriesMap(OrderModel order)
    {
        Map<AbstractOrderEntryModel, Long> uncancelableEntriesMap = new HashMap<>();
        for(ConsignmentModel cm : order.getConsignments())
        {
            appendUncancelableEntriesMap(uncancelableEntriesMap, cm);
        }
        return uncancelableEntriesMap;
    }


    protected void appendUncancelableEntriesMap(Map<AbstractOrderEntryModel, Long> uncancelableEntriesMap, ConsignmentModel consignment)
    {
        boolean consignmentUnavailableForCancel = (ConsignmentStatus.SHIPPED.equals(consignment.getStatus()) || ConsignmentStatus.CANCELLED.equals(consignment.getStatus()));
        for(ConsignmentEntryModel cem : consignment.getConsignmentEntries())
        {
            appendUncancelableEntriesMap(uncancelableEntriesMap, consignmentUnavailableForCancel, cem);
        }
    }


    protected void appendUncancelableEntriesMap(Map<AbstractOrderEntryModel, Long> uncancelableEntriesMap, boolean consignmentUnavailableForCancel, ConsignmentEntryModel consignmentEntry)
    {
        if(consignmentUnavailableForCancel)
        {
            mergeEntries(uncancelableEntriesMap, consignmentEntry.getOrderEntry(), consignmentEntry.getQuantity());
        }
        else if(consignmentEntry.getShippedQuantity() != null && consignmentEntry.getShippedQuantity().longValue() > 0L)
        {
            mergeEntries(uncancelableEntriesMap, consignmentEntry.getOrderEntry(), consignmentEntry.getShippedQuantity());
        }
    }


    protected void mergeEntries(Map<AbstractOrderEntryModel, Long> unavailableEntries, AbstractOrderEntryModel entry, Long unavailableQuantity)
    {
        long newUnavailableQuantity;
        if(unavailableQuantity == null)
        {
            return;
        }
        if(unavailableEntries.containsKey(entry))
        {
            newUnavailableQuantity = ((Long)unavailableEntries.get(entry)).longValue() + unavailableQuantity.longValue();
        }
        else
        {
            newUnavailableQuantity = unavailableQuantity.longValue();
        }
        unavailableEntries.put(entry, Long.valueOf(newUnavailableQuantity));
    }
}
