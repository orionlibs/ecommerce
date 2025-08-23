package de.hybris.platform.y2ysync.backoffice.actions.clone;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.engine.impl.AbstractComponentWidgetAdapterAware;
import de.hybris.platform.y2ysync.backoffice.data.Y2YCloneContainerForm;
import de.hybris.y2ysync.model.Y2YStreamConfigurationContainerModel;
import org.apache.log4j.Logger;

public class CloneStreamContainerAction extends AbstractComponentWidgetAdapterAware implements CockpitAction<Y2YStreamConfigurationContainerModel, Object>
{
    private static final Logger LOG = Logger.getLogger(CloneStreamContainerAction.class);
    public static final String SOCKET_OUT_CLONE_CONTAINER_FORM = "cloneContainerForm";


    public ActionResult<Object> perform(ActionContext<Y2YStreamConfigurationContainerModel> ctx)
    {
        ActionResult<Object> result;
        Y2YStreamConfigurationContainerModel sourceContainer = (Y2YStreamConfigurationContainerModel)ctx.getData();
        if(sourceContainer != null)
        {
            Y2YCloneContainerForm cloneForm = new Y2YCloneContainerForm();
            cloneForm.setSourceContainer(sourceContainer);
            result = new ActionResult("success");
            LOG.info("starting cloneStreamContainer action for container: " + sourceContainer.getId());
            sendOutput("cloneContainerForm", cloneForm);
        }
        else
        {
            LOG.error("Cloning container not possible: Source container could not be found");
            result = new ActionResult("error");
        }
        return result;
    }


    public boolean canPerform(ActionContext<Y2YStreamConfigurationContainerModel> ctx)
    {
        return true;
    }


    public boolean needsConfirmation(ActionContext<Y2YStreamConfigurationContainerModel> ctx)
    {
        return true;
    }


    public String getConfirmationMessage(ActionContext<Y2YStreamConfigurationContainerModel> ctx)
    {
        return ctx.getLabel("report.confirm");
    }
}
