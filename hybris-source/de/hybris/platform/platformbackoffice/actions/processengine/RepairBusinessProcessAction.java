package de.hybris.platform.platformbackoffice.actions.processengine;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.engine.impl.AbstractComponentWidgetAdapterAware;
import de.hybris.platform.platformbackoffice.widgets.processengine.RepairProcessForm;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RepairBusinessProcessAction extends AbstractComponentWidgetAdapterAware implements CockpitAction<BusinessProcessModel, Object>
{
    private static final Logger LOG = LoggerFactory.getLogger(RepairBusinessProcessAction.class);


    public ActionResult<Object> perform(ActionContext<BusinessProcessModel> ctx)
    {
        BusinessProcessModel businessProcess = (BusinessProcessModel)ctx.getData();
        ActionResult<Object> result = null;
        if(businessProcess != null)
        {
            LOG.debug("Repairing business process: {}", businessProcess);
            RepairProcessForm repairForm = new RepairProcessForm();
            repairForm.setBusinessProcess(businessProcess);
            result = new ActionResult("success");
            sendOutput("repairProcessForm", repairForm);
        }
        else
        {
            result = new ActionResult("error");
        }
        return result;
    }


    public boolean canPerform(ActionContext<BusinessProcessModel> ctx)
    {
        return true;
    }


    public boolean needsConfirmation(ActionContext<BusinessProcessModel> ctx)
    {
        return false;
    }


    public String getConfirmationMessage(ActionContext<BusinessProcessModel> ctx)
    {
        return null;
    }
}
