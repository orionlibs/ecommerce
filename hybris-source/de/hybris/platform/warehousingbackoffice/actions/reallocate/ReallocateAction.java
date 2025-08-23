package de.hybris.platform.warehousingbackoffice.actions.reallocate;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.engine.impl.AbstractComponentWidgetAdapterAware;
import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import java.util.List;
import javax.annotation.Resource;
import org.apache.commons.collections.CollectionUtils;

public class ReallocateAction extends AbstractComponentWidgetAdapterAware implements CockpitAction<ConsignmentModel, ConsignmentModel>
{
    protected static final String CAPTURE_PAYMENT_ON_CONSIGNMENT = "warehousing.capturepaymentonconsignment";
    protected static final String SOCKET_OUT_CONTEXT = "reallocateContext";
    @Resource
    private List<ConsignmentStatus> reallocableConsignmentStatuses;
    @Resource
    private ConfigurationService configurationService;


    public boolean canPerform(ActionContext<ConsignmentModel> actionContext)
    {
        Object data = actionContext.getData();
        boolean decision = false;
        boolean captureOnConsignmentReallocationAllowed = true;
        if(data instanceof ConsignmentModel)
        {
            ConsignmentModel consignment = (ConsignmentModel)data;
            if(!CollectionUtils.isEmpty(consignment.getConsignmentEntries()) &&
                            !(consignment.getDeliveryMode() instanceof de.hybris.platform.commerceservices.model.PickUpDeliveryModeModel) && consignment.getFulfillmentSystemConfig() == null)
            {
                decision = consignment.getConsignmentEntries().stream().anyMatch(consignmentEntry -> (consignmentEntry.getQuantityPending().longValue() > 0L));
            }
            if(getConfigurationService().getConfiguration().getBoolean("warehousing.capturepaymentonconsignment", Boolean.FALSE).booleanValue())
            {
                captureOnConsignmentReallocationAllowed = getReallocableConsignmentStatuses().contains(consignment.getStatus());
            }
        }
        return (decision && captureOnConsignmentReallocationAllowed);
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
        sendOutput("reallocateContext", actionContext.getData());
        ActionResult<ConsignmentModel> actionResult = new ActionResult("success");
        return actionResult;
    }


    protected ConfigurationService getConfigurationService()
    {
        return this.configurationService;
    }


    protected List<ConsignmentStatus> getReallocableConsignmentStatuses()
    {
        return this.reallocableConsignmentStatuses;
    }
}
