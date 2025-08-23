package de.hybris.platform.ordercancel;

import de.hybris.platform.ordercancel.model.OrderCancelRecordEntryModel;

public interface OrderStatusChangeStrategy
{
    void changeOrderStatusAfterCancelOperation(OrderCancelRecordEntryModel paramOrderCancelRecordEntryModel, boolean paramBoolean);
}
