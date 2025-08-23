package de.hybris.platform.ordercancel.events;

import de.hybris.platform.ordercancel.model.OrderCancelRecordEntryModel;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;

public class CancelPendingEvent extends AbstractEvent
{
    private final OrderCancelRecordEntryModel cancelRequestRecordEntry;


    public CancelPendingEvent(OrderCancelRecordEntryModel cancelRequestRecordEntry)
    {
        this.cancelRequestRecordEntry = cancelRequestRecordEntry;
    }


    public OrderCancelRecordEntryModel getCancelRequestRecordEntry()
    {
        return this.cancelRequestRecordEntry;
    }
}
