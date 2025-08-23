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
import javax.annotation.Resource;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

public class AcceptGoodsAction extends AbstractComponentWidgetAdapterAware implements CockpitAction<ReturnRequestModel, ReturnRequestModel>
{
    private static final Logger LOG = Logger.getLogger(AcceptGoodsAction.class);
    protected static final String ACCEPT_GOODS_SUCCESS = "customersupportbackoffice.returnrequest.accept.goods.success";
    protected static final String ACCEPT_GOODS_FAILURE = "customersupportbackoffice.returnrequest.accept.goods.failure";
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
        ReturnActionResponse returnActionResponse = new ReturnActionResponse((ReturnRequestModel)actionContext.getData());
        try
        {
            getReturnCallbackService().onReturnReceptionResponse(returnActionResponse);
            getNotificationService().notifyUser("", "JustMessage", NotificationEvent.Level.SUCCESS, new Object[] {actionContext
                            .getLabel("customersupportbackoffice.returnrequest.accept.goods.success")});
            actionResult = new ActionResult("success");
        }
        catch(OrderReturnException e)
        {
            LOG.error(e.getMessage(), (Throwable)e);
            getNotificationService().notifyUser("", "JustMessage", NotificationEvent.Level.FAILURE, new Object[] {actionContext
                            .getLabel("customersupportbackoffice.returnrequest.accept.goods.failure")});
            actionResult = new ActionResult("error");
        }
        actionResult.getStatusFlags().add(ActionResult.StatusFlag.OBJECT_PERSISTED);
        return actionResult;
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
