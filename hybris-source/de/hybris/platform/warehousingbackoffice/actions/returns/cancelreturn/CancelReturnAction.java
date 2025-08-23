package de.hybris.platform.warehousingbackoffice.actions.returns.cancelreturn;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.engine.impl.AbstractComponentWidgetAdapterAware;
import de.hybris.platform.basecommerce.enums.ReturnStatus;
import de.hybris.platform.returns.model.ReturnRequestModel;

public class CancelReturnAction extends AbstractComponentWidgetAdapterAware implements CockpitAction<ReturnRequestModel, ReturnRequestModel>
{
    protected static final String WAREHOUSING_SOCKET_OUT_CONTEXT = "warehousingCancelReturnContext";


    public boolean canPerform(ActionContext<ReturnRequestModel> actionContext)
    {
        Object data = actionContext.getData();
        boolean decision = false;
        if(data instanceof ReturnRequestModel)
        {
            ReturnStatus returnStatus = ((ReturnRequestModel)data).getStatus();
            decision = (returnStatus.equals(ReturnStatus.WAIT) || returnStatus.equals(ReturnStatus.PAYMENT_REVERSAL_FAILED));
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
        sendOutput("warehousingCancelReturnContext", returnToUpdate);
        ActionResult<ReturnRequestModel> actionResult = new ActionResult("success");
        actionResult.getStatusFlags().add(ActionResult.StatusFlag.OBJECT_PERSISTED);
        return actionResult;
    }
}
