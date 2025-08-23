package de.hybris.platform.ordercancel;

import de.hybris.platform.ordercancel.model.OrderCancelRecordEntryModel;

public interface OrderCancelNotificationServiceAdapter
{
    void sendCancelFinishedNotifications(OrderCancelRecordEntryModel paramOrderCancelRecordEntryModel);


    void sendCancelPendingNotifications(OrderCancelRecordEntryModel paramOrderCancelRecordEntryModel);
}
