package de.hybris.platform.ordercancel.impl.executors;

import de.hybris.platform.basecommerce.enums.OrderEntryStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordercancel.OrderCancelEntry;
import de.hybris.platform.ordercancel.OrderCancelException;
import de.hybris.platform.ordercancel.OrderCancelNotificationServiceAdapter;
import de.hybris.platform.ordercancel.OrderCancelPaymentServiceAdapter;
import de.hybris.platform.ordercancel.OrderCancelRecordsHandler;
import de.hybris.platform.ordercancel.OrderCancelResponse;
import de.hybris.platform.ordercancel.OrderCancelResponseExecutor;
import de.hybris.platform.ordercancel.OrderStatusChangeStrategy;
import de.hybris.platform.ordercancel.OrderUtils;
import de.hybris.platform.ordercancel.model.OrderCancelRecordEntryModel;
import de.hybris.platform.servicelayer.model.ModelService;
import org.apache.log4j.Logger;

public class WarehouseResponseExecutor implements OrderCancelResponseExecutor, NotificationServiceAdapterDependent, PaymentServiceAdapterDependent
{
    private static final Logger LOG = Logger.getLogger(WarehouseResponseExecutor.class.getName());
    private ModelService modelService;
    private OrderCancelRecordsHandler orderCancelRecordsHandler;
    private OrderCancelPaymentServiceAdapter paymentServiceAdapter;
    private OrderCancelNotificationServiceAdapter notificationServiceAdapter;
    private OrderStatusChangeStrategy completeCancelStatusChangeStrategy;
    private OrderStatusChangeStrategy partialCancelStatusChangeStrategy;
    private OrderStatusChangeStrategy warehouseDenialStatusChangeStrategy;
    private OrderStatusChangeStrategy warehouseErrorStatusChangeStrategy;


    public void processCancelResponse(OrderCancelResponse orderCancelResponse, OrderCancelRecordEntryModel cancelRequestRecordEntry) throws OrderCancelException
    {
        realizeCancelAfterWarehouseResponse(orderCancelResponse);
    }


    protected void realizeCancelAfterWarehouseResponse(OrderCancelResponse cancelResponse) throws OrderCancelException
    {
        OrderCancelRecordEntryModel pendingRecord = this.orderCancelRecordsHandler.updateRecordEntry(cancelResponse);
        switch(null.$SwitchMap$de$hybris$platform$ordercancel$OrderCancelResponse$ResponseStatus[cancelResponse.getResponseStatus().ordinal()])
        {
            case 1:
                if(this.warehouseDenialStatusChangeStrategy != null)
                {
                    this.warehouseDenialStatusChangeStrategy.changeOrderStatusAfterCancelOperation(pendingRecord, true);
                }
                return;
            case 2:
                if(this.warehouseErrorStatusChangeStrategy != null)
                {
                    this.warehouseErrorStatusChangeStrategy.changeOrderStatusAfterCancelOperation(pendingRecord, true);
                }
                throw new OrderCancelException(cancelResponse.getOrder().getCode(), "Order could not be cancelled due to :" + cancelResponse
                                .getNotes());
        }
        finalizeCancelProcessing(cancelResponse, pendingRecord);
    }


    protected void finalizeCancelProcessing(OrderCancelResponse orderCancelResponse, OrderCancelRecordEntryModel cancelRequestRecordEntry)
    {
        modifyOrderAccordingToRequest(orderCancelResponse);
        OrderModel order = orderCancelResponse.getOrder();
        this.modelService.refresh(order);
        if(!OrderUtils.hasLivingEntries((AbstractOrderModel)order))
        {
            if(this.completeCancelStatusChangeStrategy != null)
            {
                this.completeCancelStatusChangeStrategy.changeOrderStatusAfterCancelOperation(cancelRequestRecordEntry, true);
            }
        }
        else if(this.partialCancelStatusChangeStrategy != null)
        {
            this.partialCancelStatusChangeStrategy.changeOrderStatusAfterCancelOperation(cancelRequestRecordEntry, true);
        }
        this.paymentServiceAdapter.recalculateOrderAndModifyPayments(orderCancelResponse.getOrder());
        if(this.notificationServiceAdapter == null)
        {
            LOG.info("order: " + orderCancelResponse.getOrder().getCode() + " has been " + (
                            orderCancelResponse.isPartialCancel() ? "partially" : "completely") + " cancelled");
        }
        else
        {
            this.notificationServiceAdapter.sendCancelFinishedNotifications(cancelRequestRecordEntry);
        }
    }


    protected void modifyOrderAccordingToRequest(OrderCancelResponse cancelResponse)
    {
        for(OrderCancelEntry oce : cancelResponse.getEntriesToCancel())
        {
            AbstractOrderEntryModel orderEntry = oce.getOrderEntry();
            long previousQuantity = orderEntry.getQuantity().longValue();
            if(oce.getCancelQuantity() <= oce.getOrderEntry().getQuantity().longValue())
            {
                orderEntry.setQuantity(Long.valueOf(previousQuantity - oce.getCancelQuantity()));
                if(orderEntry.getQuantity().equals(Long.valueOf(0L)))
                {
                    orderEntry.setQuantityStatus(OrderEntryStatus.DEAD);
                }
                this.modelService.save(orderEntry);
                continue;
            }
            throw new IllegalStateException("Error while cancelling order [" + cancelResponse.getOrder().getCode() + "] Trying to cancel " + oce
                            .getCancelQuantity() + ", whereas orderEntry (" + orderEntry.getPk() + ") has quantity of " + previousQuantity);
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


    public OrderCancelPaymentServiceAdapter getPaymentServiceAdapter()
    {
        return this.paymentServiceAdapter;
    }


    public void setPaymentServiceAdapter(OrderCancelPaymentServiceAdapter paymentServiceAdapter)
    {
        this.paymentServiceAdapter = paymentServiceAdapter;
    }


    public OrderCancelNotificationServiceAdapter getNotificationServiceAdapter()
    {
        return this.notificationServiceAdapter;
    }


    public void setNotificationServiceAdapter(OrderCancelNotificationServiceAdapter notificationServiceAdapter)
    {
        this.notificationServiceAdapter = notificationServiceAdapter;
    }


    public OrderCancelRecordsHandler getOrderCancelRecordsHandler()
    {
        return this.orderCancelRecordsHandler;
    }


    public void setOrderCancelRecordsHandler(OrderCancelRecordsHandler orderCancelRecordsHandler)
    {
        this.orderCancelRecordsHandler = orderCancelRecordsHandler;
    }


    public OrderStatusChangeStrategy getCompleteCancelStatusChangeStrategy()
    {
        return this.completeCancelStatusChangeStrategy;
    }


    public void setCompleteCancelStatusChangeStrategy(OrderStatusChangeStrategy completeCancelStatusChangeStrategy)
    {
        this.completeCancelStatusChangeStrategy = completeCancelStatusChangeStrategy;
    }


    public OrderStatusChangeStrategy getPartialCancelStatusChangeStrategy()
    {
        return this.partialCancelStatusChangeStrategy;
    }


    public void setPartialCancelStatusChangeStrategy(OrderStatusChangeStrategy partialCancelStatusChangeStrategy)
    {
        this.partialCancelStatusChangeStrategy = partialCancelStatusChangeStrategy;
    }


    public OrderStatusChangeStrategy getWarehouseDenialStatusChangeStrategy()
    {
        return this.warehouseDenialStatusChangeStrategy;
    }


    public void setWarehouseDenialStatusChangeStrategy(OrderStatusChangeStrategy warehouseDenialStatusChangeStrategy)
    {
        this.warehouseDenialStatusChangeStrategy = warehouseDenialStatusChangeStrategy;
    }


    public OrderStatusChangeStrategy getWarehouseErrorStatusChangeStrategy()
    {
        return this.warehouseErrorStatusChangeStrategy;
    }


    public void setWarehouseErrorStatusChangeStrategy(OrderStatusChangeStrategy warehouseErrorStatusChangeStrategy)
    {
        this.warehouseErrorStatusChangeStrategy = warehouseErrorStatusChangeStrategy;
    }
}
