package de.hybris.platform.ordercancel.impl.executors;

import de.hybris.platform.basecommerce.enums.OrderEntryStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.order.CalculationService;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.ordercancel.OrderCancelEntry;
import de.hybris.platform.ordercancel.OrderCancelException;
import de.hybris.platform.ordercancel.OrderCancelNotificationServiceAdapter;
import de.hybris.platform.ordercancel.OrderCancelPaymentServiceAdapter;
import de.hybris.platform.ordercancel.OrderCancelRecordsHandler;
import de.hybris.platform.ordercancel.OrderCancelRequest;
import de.hybris.platform.ordercancel.OrderCancelRequestExecutor;
import de.hybris.platform.ordercancel.OrderCancelResponse;
import de.hybris.platform.ordercancel.OrderStatusChangeStrategy;
import de.hybris.platform.ordercancel.OrderUtils;
import de.hybris.platform.ordercancel.model.OrderCancelRecordEntryModel;
import de.hybris.platform.servicelayer.model.ModelService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class ImmediateCancelRequestExecutor implements OrderCancelRequestExecutor, NotificationServiceAdapterDependent, PaymentServiceAdapterDependent
{
    private static final Logger LOG = Logger.getLogger(ImmediateCancelRequestExecutor.class.getName());
    private ModelService modelService;
    private OrderCancelPaymentServiceAdapter paymentServiceAdapter;
    private OrderCancelNotificationServiceAdapter notificationServiceAdapter;
    private OrderCancelRecordsHandler orderCancelRecordsHandler;
    private OrderStatusChangeStrategy completeCancelStatusChangeStrategy;
    private OrderStatusChangeStrategy partialCancelStatusChangeStrategy;
    private CalculationService calculationService;


    public void processCancelRequest(OrderCancelRequest orderCancelRequest, OrderCancelRecordEntryModel cancelRequestRecordEntry) throws OrderCancelException
    {
        modifyOrderAccordingToRequest(orderCancelRequest);
        OrderModel order = orderCancelRequest.getOrder();
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
        if(this.paymentServiceAdapter != null)
        {
            this.paymentServiceAdapter.recalculateOrderAndModifyPayments(orderCancelRequest.getOrder());
        }
        else
        {
            LOG.debug("Missing OrderCancelPaymentServiceAdapter!");
            try
            {
                getCalculationService().calculateTotals((AbstractOrderModel)order, true);
                LOG.info("Recalculating totals using calculation service. It is recommended to have a custom OrderCancelPaymentServiceAdapter implementation based on your business requirements");
            }
            catch(CalculationException e)
            {
                LOG.error("An error occurred during order " + order.getCode() + " recalculation: {} " + e.getMessage());
            }
        }
        if(this.notificationServiceAdapter == null)
        {
            LOG.info("order: " + orderCancelRequest.getOrder().getCode() + " has been " + (
                            !orderCancelRequest.isPartialCancel() ? "completely" : "partially") + " cancelled");
        }
        else
        {
            this.notificationServiceAdapter.sendCancelFinishedNotifications(cancelRequestRecordEntry);
        }
        this.orderCancelRecordsHandler.updateRecordEntry(makeInternalResponse(orderCancelRequest, true, null));
        updateOrderProcess(orderCancelRequest);
    }


    protected void updateOrderProcess(OrderCancelRequest orderCancelRequest)
    {
        LOG.info("Not updating the order process. Please provide your own implementation");
    }


    protected void modifyOrderAccordingToRequest(OrderCancelRequest cancelRequest) throws OrderCancelException
    {
        for(OrderCancelEntry oce : cancelRequest.getEntriesToCancel())
        {
            AbstractOrderEntryModel orderEntry = oce.getOrderEntry();
            long previousQuantity = orderEntry.getQuantity().longValue();
            if(oce.getCancelQuantity() <= oce.getOrderEntry().getQuantity().longValue())
            {
                orderEntry.setQuantity(Long.valueOf(previousQuantity - oce.getCancelQuantity()));
                if(previousQuantity == oce.getCancelQuantity())
                {
                    orderEntry.setQuantityStatus(OrderEntryStatus.DEAD);
                }
                this.modelService.save(orderEntry);
                continue;
            }
            throw new OrderCancelException(cancelRequest.getOrder().getCode(), "Trying to cancel " + oce.getCancelQuantity() + ", whereas orderEntry (" + orderEntry
                            .getPk() + ") has quantity of " + previousQuantity);
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


    protected OrderCancelResponse makeInternalResponse(OrderCancelRequest request, boolean success, String message)
    {
        if(request.isPartialCancel())
        {
            return new OrderCancelResponse(request.getOrder(), request.getEntriesToCancel(),
                            success ? OrderCancelResponse.ResponseStatus.partial : OrderCancelResponse.ResponseStatus.error, message);
        }
        return new OrderCancelResponse(request.getOrder(), success ? OrderCancelResponse.ResponseStatus.full : OrderCancelResponse.ResponseStatus.error, message);
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


    protected CalculationService getCalculationService()
    {
        return this.calculationService;
    }


    @Required
    public void setCalculationService(CalculationService calculationService)
    {
        this.calculationService = calculationService;
    }
}
