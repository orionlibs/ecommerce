package de.hybris.platform.omsbackoffice.actions.order;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.engine.impl.AbstractComponentWidgetAdapterAware;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.List;
import javax.annotation.Resource;

public class TriggerSourcingAction extends AbstractComponentWidgetAdapterAware implements CockpitAction<OrderModel, OrderModel>
{
    protected static final String SOCKET_OUT_CONTEXT = "triggerSourcingContext";
    protected static final String SOURCING_AGAIN_CONFIRMATION_MESSAGE = "re.source.confirmation.message";
    @Resource
    private ModelService modelService;
    @Resource
    private List<OrderStatus> validOrderStatusForResourcing;


    public boolean canPerform(ActionContext<OrderModel> actionContext)
    {
        Object data = actionContext.getData();
        OrderModel order = null;
        boolean decision = false;
        if(data instanceof OrderModel)
        {
            order = (OrderModel)data;
            if(order.getStatus() != null && getValidOrderStatusForResourcing().contains(order.getStatus()))
            {
                decision = true;
            }
        }
        return decision;
    }


    public String getConfirmationMessage(ActionContext<OrderModel> actionContext)
    {
        return actionContext.getLabel("re.source.confirmation.message");
    }


    public boolean needsConfirmation(ActionContext<OrderModel> actionContext)
    {
        return true;
    }


    public ActionResult<OrderModel> perform(ActionContext<OrderModel> actionContext)
    {
        OrderModel order = (OrderModel)actionContext.getData();
        sendOutput("triggerSourcingContext", order);
        ActionResult<OrderModel> actionResult = new ActionResult("success");
        actionResult.getStatusFlags().add(ActionResult.StatusFlag.OBJECT_PERSISTED);
        return actionResult;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    protected List<OrderStatus> getValidOrderStatusForResourcing()
    {
        return this.validOrderStatusForResourcing;
    }
}
