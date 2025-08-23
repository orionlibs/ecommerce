package de.hybris.platform.warehousingbackoffice.actions.returns.acceptgoods;

import com.hybris.backoffice.widgets.notificationarea.NotificationService;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.engine.impl.AbstractComponentWidgetAdapterAware;
import de.hybris.platform.basecommerce.enums.ReturnStatus;
import de.hybris.platform.returns.OrderReturnException;
import de.hybris.platform.returns.ReturnActionResponse;
import de.hybris.platform.returns.ReturnCallbackService;
import de.hybris.platform.returns.model.ReturnRequestModel;
import de.hybris.platform.servicelayer.model.ModelService;
import javax.annotation.Resource;
import org.apache.commons.collections.CollectionUtils;

public class AcceptGoodsAction extends AbstractComponentWidgetAdapterAware implements CockpitAction<ReturnRequestModel, ReturnRequestModel>
{
    protected static final String ACCEPT_GOODS_SUCCESS = "warehousingbackoffice.returnrequest.accept.goods.success";
    protected static final String ACCEPT_GOODS_FAILURE = "warehousingbackoffice.returnrequest.accept.goods.failure";
    protected static final String ACCEPT_GOODS_MODIFIED_FAILURE = "warehousingbackoffice.returnrequest.accept.goods.modified.failure";
    @Resource
    private ModelService modelService;
    @Resource
    private ReturnCallbackService returnCallbackService;
    @Resource
    private NotificationService notificationService;


    public boolean canPerform(ActionContext<ReturnRequestModel> actionContext)
    {
        Object data = actionContext.getData();
        ReturnRequestModel returnRequest = null;
        boolean decision = false;
        if(data instanceof ReturnRequestModel)
        {
            returnRequest = (ReturnRequestModel)data;
            if((returnRequest.getReturnEntries() != null || !CollectionUtils.isEmpty(returnRequest.getReturnEntries())) && returnRequest
                            .getStatus().equals(ReturnStatus.WAIT))
            {
                decision = true;
            }
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
        ActionResult<ReturnRequestModel> actionResult;
        ReturnRequestModel returnRequest = (ReturnRequestModel)actionContext.getData();
        getModelService().refresh(returnRequest);
        if(canPerform(actionContext))
        {
            ReturnActionResponse returnActionResponse = new ReturnActionResponse(returnRequest);
            try
            {
                getReturnCallbackService().onReturnReceptionResponse(returnActionResponse);
                getNotificationService()
                                .notifyUser("", "JustMessage", NotificationEvent.Level.SUCCESS, new Object[] {actionContext.getLabel("warehousingbackoffice.returnrequest.accept.goods.success")});
                actionResult = new ActionResult("success");
            }
            catch(OrderReturnException e)
            {
                getNotificationService()
                                .notifyUser("", "JustMessage", NotificationEvent.Level.FAILURE, new Object[] {actionContext.getLabel("warehousingbackoffice.returnrequest.accept.goods.failure")});
                actionResult = new ActionResult("error");
            }
        }
        else
        {
            getNotificationService()
                            .notifyUser("", "JustMessage", NotificationEvent.Level.FAILURE, new Object[] {actionContext.getLabel("warehousingbackoffice.returnrequest.accept.goods.modified.failure")});
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


    protected NotificationService getNotificationService()
    {
        return this.notificationService;
    }
}
