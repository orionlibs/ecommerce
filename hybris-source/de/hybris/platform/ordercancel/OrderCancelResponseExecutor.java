package de.hybris.platform.ordercancel;

import de.hybris.platform.ordercancel.model.OrderCancelRecordEntryModel;

public interface OrderCancelResponseExecutor
{
    void processCancelResponse(OrderCancelResponse paramOrderCancelResponse, OrderCancelRecordEntryModel paramOrderCancelRecordEntryModel) throws OrderCancelException;
}
