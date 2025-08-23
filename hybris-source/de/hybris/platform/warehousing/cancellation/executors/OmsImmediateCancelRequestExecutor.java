package de.hybris.platform.warehousing.cancellation.executors;

import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordercancel.OrderCancelRequest;
import de.hybris.platform.ordercancel.OrderCancelResponse;
import de.hybris.platform.ordercancel.OrderUtils;
import de.hybris.platform.ordercancel.impl.executors.ImmediateCancelRequestExecutor;
import de.hybris.platform.processengine.BusinessProcessEvent;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.processengine.model.ProcessTaskModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.Collection;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

public class OmsImmediateCancelRequestExecutor extends ImmediateCancelRequestExecutor
{
    protected static final String WAIT_FOR_MANUAL_ORDER_CHECK_ACTION = "waitForManualOrderCheckCSA";
    protected static final String CSA_VERIFIED_ORDER_EVENT_NAME = "CSAOrderVerified";
    protected static final String WAIT_FOR_ORDER_ACTION = "waitForOrderAction";
    protected static final String ORDER_ACTION_EVENT_NAME = "OrderActionEvent";
    protected static final String CANCELLED_CHOICE = "cancelled";
    private static final Logger LOG = LoggerFactory.getLogger(OmsImmediateCancelRequestExecutor.class.getName());
    private BusinessProcessService businessProcessService;


    protected void updateOrderProcess(OrderCancelRequest orderCancelRequest)
    {
        ServicesUtil.validateParameterNotNull(orderCancelRequest, "No Order Cancellation Request found to be cancelled");
        ServicesUtil.validateParameterNotNull(orderCancelRequest.getOrder(), "No Order found to be cancelled");
        OrderModel order = orderCancelRequest.getOrder();
        if(!OrderUtils.hasLivingEntries((AbstractOrderModel)order) || OrderStatus.CANCELLED.equals(order.getStatus()))
        {
            LOG.info("Moving order process as complete order cancellation is requested for the order: [{}]", order.getCode());
            order.getOrderProcess().stream()
                            .filter(process -> process.getCode().startsWith(order.getStore().getSubmitOrderProcessCode()))
                            .forEach(filteredProcess -> {
                                Collection<ProcessTaskModel> currentTasks = filteredProcess.getCurrentTasks();
                                Assert.isTrue(CollectionUtils.isNotEmpty(currentTasks), String.format("No available process tasks found for the Order to be cancelled [%s]", new Object[] {order.getCode()}));
                                if(currentTasks.stream().anyMatch(()))
                                {
                                    getBusinessProcessService().triggerEvent(BusinessProcessEvent.builder(filteredProcess.getCode() + "_OrderActionEvent").withChoice("cancelled").withEventTriggeringInTheFutureDisabled().build());
                                }
                                else if(currentTasks.stream().anyMatch(()))
                                {
                                    getBusinessProcessService().triggerEvent(filteredProcess.getCode() + "_CSAOrderVerified");
                                }
                            });
        }
        else
        {
            LOG.info("Performing immediate partial cancellation. No update required on the order process");
        }
    }


    protected OrderCancelResponse makeInternalResponse(OrderCancelRequest request, boolean success, String message)
    {
        OrderCancelResponse.ResponseStatus orderCancelResponseStatus = OrderCancelResponse.ResponseStatus.full;
        if(request.isPartialCancel())
        {
            orderCancelResponseStatus = OrderCancelResponse.ResponseStatus.partial;
        }
        return new OrderCancelResponse(request.getOrder(), request.getEntriesToCancel(),
                        success ? orderCancelResponseStatus : OrderCancelResponse.ResponseStatus.error, message);
    }


    protected BusinessProcessService getBusinessProcessService()
    {
        return this.businessProcessService;
    }


    @Required
    public void setBusinessProcessService(BusinessProcessService businessProcessService)
    {
        this.businessProcessService = businessProcessService;
    }
}
