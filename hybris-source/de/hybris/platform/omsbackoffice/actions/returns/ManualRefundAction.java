package de.hybris.platform.omsbackoffice.actions.returns;

import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.engine.impl.AbstractComponentWidgetAdapterAware;
import com.hybris.cockpitng.util.notifications.NotificationService;
import de.hybris.platform.basecommerce.enums.ReturnStatus;
import de.hybris.platform.returns.OrderReturnException;
import de.hybris.platform.returns.ReturnService;
import de.hybris.platform.returns.model.ReturnRequestModel;
import de.hybris.platform.servicelayer.model.ModelService;
import javax.annotation.Resource;
import org.apache.log4j.Logger;

public class ManualRefundAction extends AbstractComponentWidgetAdapterAware implements CockpitAction<ReturnRequestModel, ReturnRequestModel>
{
    private static final Logger LOG = Logger.getLogger(ManualRefundAction.class);
    protected static final String SUCCESS_MESSAGE_LABEL = "action.manualrefund.success";
    protected static final String FAILURE_MESSAGE_LABEL = "action.manualrefund.failure";
    protected static final String UNSAVED_OBJECT_WARNING_LABEL = "action.manualrefund.unsaved.object.warning";
    @Resource
    private ModelService modelService;
    @Resource
    private ReturnService returnService;
    @Resource
    private NotificationService notificationService;


    public boolean canPerform(ActionContext<ReturnRequestModel> actionContext)
    {
        Object data = actionContext.getData();
        boolean decision = false;
        if(data instanceof ReturnRequestModel)
        {
            ReturnStatus returnStatus = ((ReturnRequestModel)data).getStatus();
            decision = (returnStatus.equals(ReturnStatus.RECEIVED) || returnStatus.equals(ReturnStatus.PAYMENT_REVERSAL_FAILED));
        }
        return decision;
    }


    public String getConfirmationMessage(ActionContext<ReturnRequestModel> actionContext)
    {
        return actionContext.getLabel("action.manualrefund.unsaved.object.warning");
    }


    public boolean needsConfirmation(ActionContext<ReturnRequestModel> actionContext)
    {
        Object data = actionContext.getData();
        return getModelService().isModified(data);
    }


    public ActionResult<ReturnRequestModel> perform(ActionContext<ReturnRequestModel> actionContext)
    {
        ActionResult<ReturnRequestModel> actionResult;
        ReturnRequestModel returnRequest = (ReturnRequestModel)actionContext.getData();
        try
        {
            getReturnService().requestManualPaymentReversalForReturnRequest(returnRequest);
            getNotificationService().notifyUser("", "JustMessage", NotificationEvent.Level.SUCCESS, new Object[] {actionContext
                            .getLabel("action.manualrefund.success")});
            actionResult = new ActionResult("success");
        }
        catch(OrderReturnException e)
        {
            LOG.error(e.getMessage(), (Throwable)e);
            getNotificationService().notifyUser("", "JustMessage", NotificationEvent.Level.FAILURE, new Object[] {actionContext
                            .getLabel("action.manualrefund.failure")});
            actionResult = new ActionResult("error");
        }
        actionResult.getStatusFlags().add(ActionResult.StatusFlag.OBJECT_PERSISTED);
        return actionResult;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    protected ReturnService getReturnService()
    {
        return this.returnService;
    }


    protected NotificationService getNotificationService()
    {
        return this.notificationService;
    }
}
