package de.hybris.platform.warehousing.allocation.impl;

import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;

public class ConsignmentEntryDeclinedQuantityHandler implements DynamicAttributeHandler<Long, ConsignmentEntryModel>
{
    public Long get(ConsignmentEntryModel consignmentEntry)
    {
        return Long.valueOf(consignmentEntry.getDeclineEntryEvents().stream().mapToLong(event -> event.getQuantity().longValue()).sum());
    }


    public void set(ConsignmentEntryModel consignmentEntry, Long value)
    {
        throw new UnsupportedOperationException();
    }
}
