package de.hybris.platform.warehousing.cancellation.impl;

import de.hybris.platform.ordercancel.OrderCancelNotificationServiceAdapter;
import de.hybris.platform.ordercancel.model.OrderCancelRecordEntryModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OmsOrderCancelNotificationServiceAdapter implements OrderCancelNotificationServiceAdapter
{
    private static final Logger LOG = LoggerFactory.getLogger(OmsOrderCancelNotificationServiceAdapter.class);


    public void sendCancelFinishedNotifications(OrderCancelRecordEntryModel cancelRequestRecordEntry)
    {
        LOG.info("Send cancel finished notification for cancelRequestRecordEntry: {}", cancelRequestRecordEntry.getCode());
    }


    public void sendCancelPendingNotifications(OrderCancelRecordEntryModel cancelRequestRecordEntry)
    {
        LOG.info("Send cancel pending notification for cancelRequestRecordEntry: {}", cancelRequestRecordEntry.getCode());
    }
}
