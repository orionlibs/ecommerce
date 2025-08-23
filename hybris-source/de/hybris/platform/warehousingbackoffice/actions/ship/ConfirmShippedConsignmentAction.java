package de.hybris.platform.warehousingbackoffice.actions.ship;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.warehousing.shipping.service.WarehousingShippingService;
import de.hybris.platform.warehousingbackoffice.actions.util.AbstractConsignmentWorkflow;
import java.util.Objects;
import javax.annotation.Resource;

public class ConfirmShippedConsignmentAction extends AbstractConsignmentWorkflow implements CockpitAction<ConsignmentModel, ConsignmentModel>
{
    public static final String SUCCESS_MESSAGE = "ship.action.message.success";
    public static final String FAILED_MESSAGE = "ship.action.message.failed";
    @Resource
    private WarehousingShippingService warehousingShippingService;


    public boolean canPerform(ActionContext<ConsignmentModel> actionContext)
    {
        Object data = actionContext.getData();
        ConsignmentModel consignment = null;
        if(data instanceof ConsignmentModel)
        {
            consignment = (ConsignmentModel)data;
        }
        if(Objects.isNull(consignment) || consignment.getDeliveryMode() instanceof de.hybris.platform.commerceservices.model.PickUpDeliveryModeModel ||
                        isFulfillmentExternal(consignment) || !getWarehousingShippingService().isConsignmentConfirmable(consignment))
        {
            return false;
        }
        return true;
    }


    public String getConfirmationMessage(ActionContext<ConsignmentModel> actionContext)
    {
        return null;
    }


    public boolean needsConfirmation(ActionContext<ConsignmentModel> actionContext)
    {
        return false;
    }


    public ActionResult<ConsignmentModel> perform(ActionContext<ConsignmentModel> actionContext)
    {
        getWarehousingShippingService().confirmShipConsignment((ConsignmentModel)actionContext.getData());
        return getConsignmentActionResult(actionContext, "ship.action.message.success", "ship.action.message.failed", ConsignmentStatus.SHIPPED);
    }


    protected WarehousingShippingService getWarehousingShippingService()
    {
        return this.warehousingShippingService;
    }
}
