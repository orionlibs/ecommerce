package de.hybris.platform.omsbackoffice.actions.returns;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.engine.impl.AbstractComponentWidgetAdapterAware;
import de.hybris.platform.basecommerce.enums.ReturnStatus;
import de.hybris.platform.returns.ReturnService;
import de.hybris.platform.returns.model.ReturnRequestModel;
import javax.annotation.Resource;

public class CancelReturnAction extends AbstractComponentWidgetAdapterAware implements CockpitAction<ReturnRequestModel, ReturnRequestModel>
{
    protected static final String CUSTOMER_SUPPORT_SOCKET_OUT_CONTEXT = "csCancelReturnContext";
    @Resource
    private ReturnService returnService;


    public boolean canPerform(ActionContext<ReturnRequestModel> actionContext)
    {
        Object data = actionContext.getData();
        boolean decision = false;
        if(data instanceof ReturnRequestModel)
        {
            ReturnStatus returnStatus = ((ReturnRequestModel)data).getStatus();
            decision = (returnStatus.equals(ReturnStatus.APPROVAL_PENDING) || returnStatus.equals(ReturnStatus.APPROVING) || returnStatus.equals(ReturnStatus.WAIT) || returnStatus.equals(ReturnStatus.PAYMENT_REVERSAL_FAILED));
        }
        return decision;
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
        ReturnRequestModel returnToUpdate = (ReturnRequestModel)actionContext.getData();
        sendOutput("csCancelReturnContext", returnToUpdate);
        ActionResult<ReturnRequestModel> actionResult = new ActionResult("success");
        actionResult.getStatusFlags().add(ActionResult.StatusFlag.OBJECT_PERSISTED);
        return actionResult;
    }


    protected ReturnService getReturnService()
    {
        return this.returnService;
    }


    public void setReturnService(ReturnService returnService)
    {
        this.returnService = returnService;
    }
}
