package de.hybris.platform.omsbackoffice.actions.order;

import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.engine.impl.AbstractComponentWidgetAdapterAware;
import com.hybris.cockpitng.util.notifications.NotificationService;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.model.ModelService;
import javax.annotation.Resource;
import org.apache.log4j.Logger;

public class ManualPaymentCaptureAction extends AbstractComponentWidgetAdapterAware implements CockpitAction<OrderModel, OrderModel>
{
    private static final Logger LOG = Logger.getLogger(ManualPaymentCaptureAction.class);
    protected static final String MANUAL_PAYMENT_CAPTURE_SUCCESS = "action.manualpaymentcapture.success";
    protected static final String MANUAL_CAPTURE_PAYMENT_EVENT = "ManualCapturePaymentEvent";
    @Resource
    private BusinessProcessService businessProcessService;
    @Resource
    private ModelService modelService;
    @Resource
    private NotificationService notificationService;


    public ActionResult<OrderModel> perform(ActionContext<OrderModel> actionContext)
    {
        ActionResult<OrderModel> actionResult = null;
        if(actionContext != null && actionContext.getData() != null)
        {
            OrderModel order = (OrderModel)actionContext.getData();
            executeManualPaymentCaptureOperation(order);
            actionResult = new ActionResult("success");
            getNotificationService()
                            .notifyUser((String)null, "JustMessage", NotificationEvent.Level.SUCCESS, new Object[] {actionContext.getLabel("action.manualpaymentcapture.success")});
            actionResult.getStatusFlags().add(ActionResult.StatusFlag.OBJECT_PERSISTED);
        }
        return actionResult;
    }


    public boolean canPerform(ActionContext<OrderModel> ctx)
    {
        OrderModel order = (OrderModel)ctx.getData();
        return (order != null && OrderStatus.PAYMENT_NOT_CAPTURED.equals(order.getStatus()));
    }


    public boolean needsConfirmation(ActionContext<OrderModel> ctx)
    {
        return false;
    }


    public String getConfirmationMessage(ActionContext<OrderModel> ctx)
    {
        return null;
    }


    protected void executeManualPaymentCaptureOperation(OrderModel order)
    {
        order.getOrderProcess().stream()
                        .filter(process -> process.getCode().startsWith(order.getStore().getSubmitOrderProcessCode())).forEach(filteredProcess -> getBusinessProcessService().triggerEvent(filteredProcess.getCode() + "_ManualCapturePaymentEvent"));
        LOG.info(String.format("Payment Capture Manual Release completed. %s triggered.", new Object[] {"ManualCapturePaymentEvent"}));
        order.setStatus(OrderStatus.PAYMENT_CAPTURED);
        getModelService().save(order);
    }


    protected BusinessProcessService getBusinessProcessService()
    {
        return this.businessProcessService;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    protected NotificationService getNotificationService()
    {
        return this.notificationService;
    }
}
