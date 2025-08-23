package de.hybris.platform.ordercancel.impl.orderstatechangingstrategies;

import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordercancel.OrderStatusChangeStrategy;
import de.hybris.platform.ordercancel.OrderUtils;
import de.hybris.platform.ordercancel.model.OrderCancelRecordEntryModel;
import de.hybris.platform.servicelayer.model.ModelService;

public class SetCancellledStrategy implements OrderStatusChangeStrategy
{
    private ModelService modelService;


    public void changeOrderStatusAfterCancelOperation(OrderCancelRecordEntryModel orderCancelRecordEntry, boolean saveOrderModel)
    {
        OrderModel order = orderCancelRecordEntry.getModificationRecord().getOrder();
        order.setStatus(OrderStatus.CANCELLED);
        clearAdditionalCostsForCompleteCancelledOrder(order);
        if(saveOrderModel)
        {
            this.modelService.save(orderCancelRecordEntry.getModificationRecord().getOrder());
        }
    }


    public ModelService getModelService()
    {
        return this.modelService;
    }


    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    protected void clearAdditionalCostsForCompleteCancelledOrder(OrderModel order)
    {
        if(!OrderUtils.hasLivingEntries((AbstractOrderModel)order))
        {
            order.setDeliveryMode(null);
            order.setDiscounts(null);
        }
    }
}
