package de.hybris.platform.warehousing.cancellation.strategy.impl;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.basecommerce.enums.OrderCancelState;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordercancel.OrderCancelStateMappingStrategy;
import de.hybris.platform.ordercancel.impl.DefaultOrderCancelStateMappingStrategy;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;

public class WarehousingOrderCancelStateMappingStrategy extends DefaultOrderCancelStateMappingStrategy implements OrderCancelStateMappingStrategy
{
    private List<ConsignmentStatus> confirmedConsignmentStatus;
    private ModelService modelService;


    public OrderCancelState getOrderCancelState(OrderModel order)
    {
        this.modelService.refresh(order);
        OrderStatus orderStatus = order.getStatus();
        if(OrderStatus.CANCELLED.equals(orderStatus) || OrderStatus.COMPLETED.equals(orderStatus))
        {
            return OrderCancelState.CANCELIMPOSSIBLE;
        }
        Collection<ConsignmentModel> consignments = order.getConsignments();
        if(consignments == null || consignments.isEmpty())
        {
            return OrderCancelState.PENDINGORHOLDINGAREA;
        }
        if(consignments.stream().allMatch(consignment ->
                        (consignment.getStatus().equals(ConsignmentStatus.READY) || consignment.getStatus().equals(ConsignmentStatus.READY_FOR_PICKUP) || consignment.getStatus().equals(ConsignmentStatus.READY_FOR_SHIPPING) || consignment.getStatus().equals(ConsignmentStatus.CANCELLED))))
        {
            return OrderCancelState.SENTTOWAREHOUSE;
        }
        return checkConsignments(consignments);
    }


    protected OrderCancelState checkConsignments(Collection<ConsignmentModel> consignments)
    {
        boolean oneShipped = false;
        boolean allShipped = true;
        boolean allReady = true;
        AbstractOrderModel orderModel = ((ConsignmentModel)consignments.iterator().next()).getOrder();
        boolean isQtyUnallocated = orderModel.getEntries().stream().anyMatch(entry -> (((OrderEntryModel)entry).getQuantityUnallocated().longValue() > 0L));
        for(ConsignmentModel consignmentModel : consignments)
        {
            ConsignmentStatus status = consignmentModel.getStatus();
            if(getConfirmedConsignmentStatus().contains(status))
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
        if(allShipped && !isQtyUnallocated)
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


    protected List<ConsignmentStatus> getConfirmedConsignmentStatus()
    {
        return this.confirmedConsignmentStatus;
    }


    @Required
    public void setConfirmedConsignmentStatus(List<ConsignmentStatus> confirmedConsignmentStatus)
    {
        this.confirmedConsignmentStatus = confirmedConsignmentStatus;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }
}
