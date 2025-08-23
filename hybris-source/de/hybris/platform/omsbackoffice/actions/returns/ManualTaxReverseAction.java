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
import javax.annotation.Resource;
import org.apache.log4j.Logger;

public class ManualTaxReverseAction extends AbstractComponentWidgetAdapterAware implements CockpitAction<ReturnRequestModel, ReturnRequestModel>
{
    private static final Logger LOG = Logger.getLogger(ManualTaxReverseAction.class);
    protected static final String SUCCESS_MESSAGE_LABEL = "action.manualtaxreverse.success";
    protected static final String FAILURE_MESSAGE_LABEL = "action.manualtaxreverse.failure";
    @Resource
    private ReturnService returnService;
    @Resource
    private NotificationService notificationService;


    public boolean canPerform(ActionContext<ReturnRequestModel> actionContext)
    {
        Object data = actionContext.getData();
        return (data instanceof ReturnRequestModel && ((ReturnRequestModel)data)
                        .getStatus().equals(ReturnStatus.TAX_REVERSAL_FAILED));
    }


    public String getConfirmationMessage(ActionContext<ReturnRequestModel> actionContext)
    {
        return null;
    }


    public boolean needsConfirmation(ActionContext<ReturnRequestModel> actionContext)
    {
        return false;
    }


    public ActionResult<ReturnRequestModel> perform(ActionContext<ReturnRequestModel> actionContext)
    {
        ActionResult<ReturnRequestModel> actionResult;
        ReturnRequestModel returnRequest = (ReturnRequestModel)actionContext.getData();
        try
        {
            getReturnService().requestManualTaxReversalForReturnRequest(returnRequest);
            getNotificationService().notifyUser("", "JustMessage", NotificationEvent.Level.SUCCESS, new Object[] {actionContext
                            .getLabel("action.manualtaxreverse.success")});
            actionResult = new ActionResult("success");
        }
        catch(OrderReturnException e)
        {
            LOG.error(e.getMessage(), (Throwable)e);
            getNotificationService().notifyUser("", "JustMessage", NotificationEvent.Level.FAILURE, new Object[] {actionContext
                            .getLabel("action.manualtaxreverse.failure")});
            actionResult = new ActionResult("error");
        }
        actionResult.getStatusFlags().add(ActionResult.StatusFlag.OBJECT_PERSISTED);
        return actionResult;
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
