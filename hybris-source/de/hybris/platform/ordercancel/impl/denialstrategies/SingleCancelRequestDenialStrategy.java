package de.hybris.platform.ordercancel.impl.denialstrategies;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.ordercancel.OrderCancelDenialReason;
import de.hybris.platform.ordercancel.OrderCancelDenialStrategy;
import de.hybris.platform.ordercancel.OrderCancelRecordsHandler;
import de.hybris.platform.ordercancel.model.OrderCancelConfigModel;
import de.hybris.platform.ordercancel.model.OrderCancelRecordModel;

public class SingleCancelRequestDenialStrategy extends AbstractCancelDenialStrategy implements OrderCancelDenialStrategy
{
    private OrderCancelRecordsHandler orderCancelRecordsHandler;


    public OrderCancelDenialReason getCancelDenialReason(OrderCancelConfigModel configuration, OrderModel order, PrincipalModel requester, boolean partialCancel, boolean partialEntryCancel)
    {
        OrderCancelRecordModel orderCancel = this.orderCancelRecordsHandler.getCancelRecord(order);
        if(orderCancel == null)
        {
            return null;
        }
        if(orderCancel.isInProgress())
        {
            return getReason();
        }
        return null;
    }


    public OrderCancelRecordsHandler getOrderCancelRecordsHandler()
    {
        return this.orderCancelRecordsHandler;
    }


    public void setOrderCancelRecordsHandler(OrderCancelRecordsHandler orderCancelRecordsHandler)
    {
        this.orderCancelRecordsHandler = orderCancelRecordsHandler;
    }
}
