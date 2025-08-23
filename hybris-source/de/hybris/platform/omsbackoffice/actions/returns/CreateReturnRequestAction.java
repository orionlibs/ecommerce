package de.hybris.platform.omsbackoffice.actions.returns;

import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.engine.impl.AbstractComponentWidgetAdapterAware;
import com.hybris.cockpitng.util.notifications.NotificationService;
import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.returns.ReturnService;
import de.hybris.platform.servicelayer.model.ModelService;
import javax.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;

public class CreateReturnRequestAction extends AbstractComponentWidgetAdapterAware implements CockpitAction<OrderModel, OrderModel>
{
    protected static final String SOCKET_OUT_CONTEXT = "createReturnRequestContext";
    protected static final String CREATE_RETURN_REQUEST_MODIFIED_FAILURE = "customersupport.create.returnrequest.modified.failure";
    @Resource
    private ReturnService returnService;
    @Resource
    private ModelService modelService;
    @Resource
    private NotificationService notificationService;


    public boolean canPerform(ActionContext<OrderModel> actionContext)
    {
        Object data = actionContext.getData();
        OrderModel order = null;
        if(data instanceof OrderModel)
        {
            order = (OrderModel)data;
        }
        return (containsConsignments(order) &&
                        !order.getConsignments().stream().noneMatch(consignment ->
                                        (ConsignmentStatus.SHIPPED.equals(consignment.getStatus()) || ConsignmentStatus.PICKUP_COMPLETE.equals(consignment.getStatus()))) &&
                        !getReturnService().getAllReturnableEntries(order).isEmpty());
    }


    private boolean containsConsignments(OrderModel order)
    {
        return (order != null && !CollectionUtils.isEmpty(order.getEntries()) && !CollectionUtils.isEmpty(order.getConsignments()));
    }


    public String getConfirmationMessage(ActionContext<OrderModel> actionContext)
    {
        return null;
    }


    public boolean needsConfirmation(ActionContext<OrderModel> actionContext)
    {
        return false;
    }


    public ActionResult<OrderModel> perform(ActionContext<OrderModel> actionContext)
    {
        ActionResult<OrderModel> actionResult;
        OrderModel order = (OrderModel)actionContext.getData();
        getModelService().refresh(order);
        if(canPerform(actionContext))
        {
            sendOutput("createReturnRequestContext", actionContext.getData());
            actionResult = new ActionResult("success");
        }
        else
        {
            this.notificationService.notifyUser(this.notificationService.getWidgetNotificationSource(actionContext), "JustMessage", NotificationEvent.Level.FAILURE, new Object[] {actionContext
                            .getLabel("customersupport.create.returnrequest.modified.failure")});
            actionResult = new ActionResult("error");
        }
        actionResult.getStatusFlags().add(ActionResult.StatusFlag.OBJECT_PERSISTED);
        return actionResult;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    protected ReturnService getReturnService()
    {
        return this.returnService;
    }
}
