package de.hybris.platform.ordercancel.impl;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.basecommerce.enums.OrderCancelState;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordercancel.OrderCancelStateMappingStrategy;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import java.util.Collection;

public class DefaultOrderCancelStateMappingStrategy implements OrderCancelStateMappingStrategy
{
    public OrderCancelState getOrderCancelState(OrderModel order)
    {
        OrderStatus orderStatus = order.getStatus();
        if(OrderStatus.CANCELLED.equals(orderStatus) || OrderStatus.CANCELLING.equals(orderStatus) || OrderStatus.COMPLETED
                        .equals(orderStatus))
        {
            return OrderCancelState.CANCELIMPOSSIBLE;
        }
        Collection<ConsignmentModel> consignments = order.getConsignments();
        if(consignments == null || consignments.isEmpty())
        {
            return OrderCancelState.PENDINGORHOLDINGAREA;
        }
        return checkConsignments(consignments);
    }


    protected OrderCancelState checkConsignments(Collection<ConsignmentModel> consignments)
    {
        boolean oneShipped = false;
        boolean allShipped = true;
        boolean allReady = true;
        for(ConsignmentModel consignmentModel : consignments)
        {
            ConsignmentStatus status = consignmentModel.getStatus();
            if(status.equals(ConsignmentStatus.SHIPPED))
            {
                oneShipped = true;
            }
            else
            {
                allShipped = false;
            }
            if(!status.equals(ConsignmentStatus.READY))
            {
                allReady = false;
            }
        }
        if(allShipped)
        {
            return OrderCancelState.CANCELIMPOSSIBLE;
        }
        if(oneShipped)
        {
            return OrderCancelState.PARTIALLYSHIPPED;
        }
        if(allReady)
        {
            return OrderCancelState.SENTTOWAREHOUSE;
        }
        return OrderCancelState.SHIPPING;
    }
}
