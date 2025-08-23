package de.hybris.platform.ordercancel.impl;

import de.hybris.platform.ordercancel.OrderCancelNotificationServiceAdapter;
import de.hybris.platform.ordercancel.events.CancelFinishedEvent;
import de.hybris.platform.ordercancel.events.CancelPendingEvent;
import de.hybris.platform.ordercancel.model.OrderCancelRecordEntryModel;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;

public class SendEventOrderCancelNotification implements OrderCancelNotificationServiceAdapter
{
    private EventService eventService;


    public void sendCancelFinishedNotifications(OrderCancelRecordEntryModel cancelRequestRecordEntry)
    {
        this.eventService.publishEvent((AbstractEvent)new CancelFinishedEvent(cancelRequestRecordEntry));
    }


    public void sendCancelPendingNotifications(OrderCancelRecordEntryModel cancelRequestRecordEntry)
    {
        this.eventService.publishEvent((AbstractEvent)new CancelPendingEvent(cancelRequestRecordEntry));
    }


    public void setEventService(EventService eventService)
    {
        this.eventService = eventService;
    }
}
