package de.hybris.platform.ordercancel.impl.orderstatechangingstrategies;

import de.hybris.platform.ordercancel.OrderStatusChangeStrategy;
import de.hybris.platform.ordercancel.model.OrderCancelRecordEntryModel;
import de.hybris.platform.servicelayer.model.ModelService;

public class RestorePreviousOrderStatusStrategy implements OrderStatusChangeStrategy
{
    private ModelService modelService;


    public void changeOrderStatusAfterCancelOperation(OrderCancelRecordEntryModel orderCancelRecordEntry, boolean saveOrderModel)
    {
        orderCancelRecordEntry.getModificationRecord().getOrder().setStatus(orderCancelRecordEntry
                        .getOriginalVersion().getPreviousOrderVersion().getStatus());
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
}
