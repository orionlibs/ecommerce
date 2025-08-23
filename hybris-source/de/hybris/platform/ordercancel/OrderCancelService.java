package de.hybris.platform.ordercancel;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.ordercancel.model.OrderCancelConfigModel;
import de.hybris.platform.ordercancel.model.OrderCancelRecordEntryModel;
import de.hybris.platform.ordercancel.model.OrderCancelRecordModel;
import java.util.Map;

public interface OrderCancelService
{
    OrderCancelConfigModel getConfiguration();


    OrderCancelRecordModel getCancelRecordForOrder(OrderModel paramOrderModel) throws OrderCancelException;


    OrderCancelRecordEntryModel getPendingCancelRecordEntry(OrderModel paramOrderModel) throws OrderCancelException;


    CancelDecision isCancelPossible(OrderModel paramOrderModel, PrincipalModel paramPrincipalModel, boolean paramBoolean1, boolean paramBoolean2);


    OrderCancelRecordEntryModel requestOrderCancel(OrderCancelRequest paramOrderCancelRequest, PrincipalModel paramPrincipalModel) throws OrderCancelException;


    Map<AbstractOrderEntryModel, Long> getAllCancelableEntries(OrderModel paramOrderModel, PrincipalModel paramPrincipalModel);
}
