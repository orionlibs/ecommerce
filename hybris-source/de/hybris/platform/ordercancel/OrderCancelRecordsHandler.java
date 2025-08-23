package de.hybris.platform.ordercancel;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.ordercancel.exceptions.OrderCancelRecordsHandlerException;
import de.hybris.platform.ordercancel.model.OrderCancelRecordEntryModel;
import de.hybris.platform.ordercancel.model.OrderCancelRecordModel;

public interface OrderCancelRecordsHandler
{
    OrderCancelRecordModel getCancelRecord(OrderModel paramOrderModel);


    OrderCancelRecordEntryModel getPendingCancelRecordEntry(OrderModel paramOrderModel) throws OrderCancelRecordsHandlerException;


    OrderCancelRecordEntryModel createRecordEntry(OrderCancelRequest paramOrderCancelRequest) throws OrderCancelRecordsHandlerException;


    OrderCancelRecordEntryModel createRecordEntry(OrderCancelRequest paramOrderCancelRequest, PrincipalModel paramPrincipalModel) throws OrderCancelRecordsHandlerException;


    OrderCancelRecordEntryModel updateRecordEntry(OrderCancelResponse paramOrderCancelResponse) throws OrderCancelRecordsHandlerException;
}
