package de.hybris.platform.ordercancel.impl.executors;

import de.hybris.platform.ordercancel.OrderCancelNotificationServiceAdapter;
import de.hybris.platform.ordercancel.OrderCancelRequest;
import de.hybris.platform.ordercancel.OrderCancelRequestExecutor;
import de.hybris.platform.ordercancel.OrderCancelWarehouseAdapter;
import de.hybris.platform.ordercancel.OrderStatusChangeStrategy;
import de.hybris.platform.ordercancel.model.OrderCancelRecordEntryModel;
import de.hybris.platform.servicelayer.model.ModelService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class WarehouseProcessingCancelRequestExecutor implements OrderCancelRequestExecutor, NotificationServiceAdapterDependent, WarehouseAdapterDependent
{
    private static final Logger LOG = Logger.getLogger(WarehouseProcessingCancelRequestExecutor.class.getName());
    private ModelService modelService;
    private OrderCancelWarehouseAdapter warehouseAdapter;
    private OrderCancelNotificationServiceAdapter notificationServiceAdapter;
    private OrderStatusChangeStrategy orderStatusChangeStrategy;


    public void processCancelRequest(OrderCancelRequest orderCancelRequest, OrderCancelRecordEntryModel cancelRequestRecordEntry)
    {
        this.orderStatusChangeStrategy.changeOrderStatusAfterCancelOperation(cancelRequestRecordEntry, true);
        this.warehouseAdapter.requestOrderCancel(orderCancelRequest);
        if(this.notificationServiceAdapter == null)
        {
            LOG.info("order: " + orderCancelRequest.getOrder().getCode() + " is being cancelled");
        }
        else
        {
            this.notificationServiceAdapter.sendCancelPendingNotifications(cancelRequestRecordEntry);
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


    public OrderCancelWarehouseAdapter getWarehouseAdapter()
    {
        return this.warehouseAdapter;
    }


    @Required
    public void setWarehouseAdapter(OrderCancelWarehouseAdapter warehouseAdapter)
    {
        this.warehouseAdapter = warehouseAdapter;
    }


    public OrderCancelNotificationServiceAdapter getNotificationServiceAdapter()
    {
        return this.notificationServiceAdapter;
    }


    public void setNotificationServiceAdapter(OrderCancelNotificationServiceAdapter notificationServiceAdapter)
    {
        this.notificationServiceAdapter = notificationServiceAdapter;
    }


    public OrderStatusChangeStrategy getOrderStatusChangeStrategy()
    {
        return this.orderStatusChangeStrategy;
    }


    @Required
    public void setOrderStatusChangeStrategy(OrderStatusChangeStrategy orderStatusChangeStrategy)
    {
        this.orderStatusChangeStrategy = orderStatusChangeStrategy;
    }
}
