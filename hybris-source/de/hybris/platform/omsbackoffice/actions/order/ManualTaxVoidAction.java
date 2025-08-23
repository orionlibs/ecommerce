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

public class ManualTaxVoidAction extends AbstractComponentWidgetAdapterAware implements CockpitAction<OrderModel, OrderModel>
{
    protected static final String MANUAL_TAX_VOID_SUCCESS = "action.manualtaxvoid.success";
    protected static final String MANUAL_VOID_TAX_EVENT = "ManualVoidTaxEvent";
    private static final Logger LOG = Logger.getLogger(ManualTaxVoidAction.class);
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
            executeManualTaxVoidOperation(order);
            actionResult = new ActionResult("success");
            actionResult.getStatusFlags().add(ActionResult.StatusFlag.OBJECT_PERSISTED);
            this.notificationService.notifyUser(this.notificationService.getWidgetNotificationSource(actionContext), "JustMessage", NotificationEvent.Level.SUCCESS, new Object[] {actionContext
                            .getLabel("action.manualtaxvoid.success")});
        }
        return actionResult;
    }


    public boolean canPerform(ActionContext<OrderModel> ctx)
    {
        OrderModel order = (OrderModel)ctx.getData();
        return (order != null && OrderStatus.TAX_NOT_VOIDED.equals(order.getStatus()));
    }


    public boolean needsConfirmation(ActionContext<OrderModel> ctx)
    {
        return false;
    }


    public String getConfirmationMessage(ActionContext<OrderModel> ctx)
    {
        return null;
    }


    protected void executeManualTaxVoidOperation(OrderModel order)
    {
        order.getOrderProcess().stream()
                        .filter(process -> process.getCode().startsWith(order.getStore().getSubmitOrderProcessCode())).forEach(filteredProcess -> getBusinessProcessService().triggerEvent(filteredProcess.getCode() + "_ManualVoidTaxEvent"));
        LOG.info(String.format("Tax Void Manual Release completed. %s triggered.", new Object[] {"ManualVoidTaxEvent"}));
        order.setStatus(OrderStatus.CANCELLED);
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
}
