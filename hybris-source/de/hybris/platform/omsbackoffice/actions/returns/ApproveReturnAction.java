package de.hybris.platform.omsbackoffice.actions.returns;

import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.engine.impl.AbstractComponentWidgetAdapterAware;
import com.hybris.cockpitng.util.notifications.NotificationService;
import de.hybris.platform.basecommerce.enums.ReturnStatus;
import de.hybris.platform.returns.OrderReturnException;
import de.hybris.platform.returns.ReturnActionResponse;
import de.hybris.platform.returns.ReturnCallbackService;
import de.hybris.platform.returns.model.ReturnRequestModel;
import de.hybris.platform.servicelayer.model.ModelService;
import javax.annotation.Resource;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

public class ApproveReturnAction extends AbstractComponentWidgetAdapterAware implements CockpitAction<ReturnRequestModel, ReturnRequestModel>
{
    protected static final String APPROVAL_SUCCESS = "customersupportbackoffice.returnrequest.approval.success";
    protected static final String APPROVAL_FAILURE = "customersupportbackoffice.returnrequest.approval.failure";
    protected static final String MODIFIED_RETURN_REQUEST = "customersupportbackoffice.returnrequest.approval.modified";
    protected static final String APPROVAL_CANCELLED_FAILURE = "customersupportbackoffice.returnrequest.approval.cancelled.failure";
    private static final Logger LOG = Logger.getLogger(ApproveReturnAction.class);
    @Resource
    private ReturnCallbackService returnCallbackService;
    @Resource
    private ModelService modelService;
    @Resource
    private NotificationService notificationService;


    public boolean canPerform(ActionContext<ReturnRequestModel> actionContext)
    {
        boolean result = false;
        ReturnRequestModel returnRequest = (ReturnRequestModel)actionContext.getData();
        if(returnRequest != null)
        {
            result = (!CollectionUtils.isEmpty(returnRequest.getReturnEntries()) && returnRequest.getStatus().equals(ReturnStatus.APPROVAL_PENDING));
        }
        return result;
    }


    public String getConfirmationMessage(ActionContext<ReturnRequestModel> actionContext)
    {
        return actionContext.getLabel("customersupportbackoffice.returnrequest.approval.modified");
    }


    public boolean needsConfirmation(ActionContext<ReturnRequestModel> actionContext)
    {
        ReturnRequestModel returnRequest = (ReturnRequestModel)actionContext.getData();
        ReturnRequestModel upToDateReturnRequest = (ReturnRequestModel)getModelService().get(returnRequest.getPk());
        boolean result = false;
        if(returnRequest.getStatus().equals(upToDateReturnRequest.getStatus()))
        {
            result = (!getModelService().isUpToDate(actionContext.getData()) || ((ReturnRequestModel)actionContext.getData()).getReturnEntries().stream().anyMatch(entry -> !getModelService().isUpToDate(entry)));
        }
        return result;
    }


    public ActionResult<ReturnRequestModel> perform(ActionContext<ReturnRequestModel> actionContext)
    {
        ActionResult<ReturnRequestModel> actionResult;
        ReturnRequestModel returnRequest = (ReturnRequestModel)actionContext.getData();
        ReturnRequestModel upToDateReturnRequest = (ReturnRequestModel)getModelService().get(returnRequest.getPk());
        if(returnRequest.getStatus().equals(upToDateReturnRequest.getStatus()))
        {
            returnRequest.getReturnEntries().forEach(entry -> getModelService().save(entry));
            getModelService().save(actionContext.getData());
            ReturnActionResponse returnActionResponse = new ReturnActionResponse(returnRequest);
            try
            {
                getReturnCallbackService().onReturnApprovalResponse(returnActionResponse);
                this.notificationService.notifyUser("", "JustMessage", NotificationEvent.Level.SUCCESS, new Object[] {actionContext
                                .getLabel("customersupportbackoffice.returnrequest.approval.success")});
                actionResult = new ActionResult("success");
            }
            catch(OrderReturnException e)
            {
                LOG.error(e.getMessage());
                this.notificationService.notifyUser("", "JustMessage", NotificationEvent.Level.FAILURE, new Object[] {actionContext
                                .getLabel("customersupportbackoffice.returnrequest.approval.failure")});
                actionResult = new ActionResult("error");
            }
        }
        else
        {
            this.notificationService
                            .notifyUser("", "JustMessage", NotificationEvent.Level.FAILURE, new Object[] {actionContext.getLabel("customersupportbackoffice.returnrequest.approval.cancelled.failure")});
            actionResult = new ActionResult("error");
        }
        actionResult.getStatusFlags().add(ActionResult.StatusFlag.OBJECT_PERSISTED);
        return actionResult;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    protected ReturnCallbackService getReturnCallbackService()
    {
        return this.returnCallbackService;
    }
}
