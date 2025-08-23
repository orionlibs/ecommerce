package de.hybris.platform.ordercancel.events;

import de.hybris.platform.ordercancel.model.OrderCancelRecordEntryModel;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;

public class CancelFinishedEvent extends AbstractEvent
{
    private final OrderCancelRecordEntryModel cancelRequestRecordEntry;


    public CancelFinishedEvent(OrderCancelRecordEntryModel cancelRequestRecordEntry)
    {
        this.cancelRequestRecordEntry = cancelRequestRecordEntry;
    }


    public OrderCancelRecordEntryModel getCancelRequestRecordEntry()
    {
        return this.cancelRequestRecordEntry;
    }
}
