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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ManualTaxCommitAction extends AbstractComponentWidgetAdapterAware implements CockpitAction<OrderModel, OrderModel>
{
    protected static final String MANUAL_TAX_COMMIT_SUCCESS = "action.manualtaxcommit.success";
    protected static final String MANUAL_TAX_COMMIT_EVENT = "ManualCommitTaxEvent";
    private static final Logger LOG = LoggerFactory.getLogger(ManualTaxCommitAction.class);
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
            executeManualTaxCommitOperation(order);
            actionResult = new ActionResult("success");
            actionResult.getStatusFlags().add(ActionResult.StatusFlag.OBJECT_PERSISTED);
            this.notificationService.notifyUser(this.notificationService.getWidgetNotificationSource(actionContext), "JustMessage", NotificationEvent.Level.SUCCESS, new Object[] {actionContext
                            .getLabel("action.manualtaxcommit.success")});
        }
        return actionResult;
    }


    public boolean canPerform(ActionContext<OrderModel> ctx)
    {
        OrderModel order = (OrderModel)ctx.getData();
        return (order != null && OrderStatus.TAX_NOT_COMMITTED.equals(order.getStatus()));
    }


    public boolean needsConfirmation(ActionContext<OrderModel> ctx)
    {
        return false;
    }


    public String getConfirmationMessage(ActionContext<OrderModel> ctx)
    {
        return null;
    }


    protected void executeManualTaxCommitOperation(OrderModel order)
    {
        order.getOrderProcess().stream()
                        .filter(process -> process.getCode().startsWith(order.getStore().getSubmitOrderProcessCode())).forEach(filteredProcess -> getBusinessProcessService().triggerEvent(filteredProcess.getCode() + "_ManualCommitTaxEvent"));
        if(LOG.isDebugEnabled())
        {
            LOG.info(String.format("Tax Commit Manual Release completed. %s triggered.", new Object[] {"ManualCommitTaxEvent"}));
        }
        order.setStatus(OrderStatus.COMPLETED);
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
