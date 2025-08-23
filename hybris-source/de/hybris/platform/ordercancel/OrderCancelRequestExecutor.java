package de.hybris.platform.ordercancel;

import de.hybris.platform.ordercancel.model.OrderCancelRecordEntryModel;

public interface OrderCancelRequestExecutor
{
    void processCancelRequest(OrderCancelRequest paramOrderCancelRequest, OrderCancelRecordEntryModel paramOrderCancelRecordEntryModel) throws OrderCancelException;
}
