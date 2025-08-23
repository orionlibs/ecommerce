package de.hybris.platform.y2ysync.backoffice.actions;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.engine.impl.AbstractComponentWidgetAdapterAware;
import de.hybris.y2ysync.model.Y2YStreamConfigurationContainerModel;
import de.hybris.y2ysync.services.DataHubConfigService;
import javax.annotation.Resource;

public class CreateDataHubConfigAction extends AbstractComponentWidgetAdapterAware implements CockpitAction<Y2YStreamConfigurationContainerModel, Object>
{
    private static final String DATAHUB_CONFIG_SOCKET = "dataHubConfigDisplayWidget";
    @Resource
    DataHubConfigService dataHubConfigService;


    public ActionResult<Object> perform(ActionContext<Y2YStreamConfigurationContainerModel> ctx)
    {
        Y2YStreamConfigurationContainerModel cvm = (Y2YStreamConfigurationContainerModel)ctx.getData();
        sendOutput("dataHubConfigDisplayWidget", cvm);
        return new ActionResult("success");
    }


    public boolean canPerform(ActionContext<Y2YStreamConfigurationContainerModel> ctx)
    {
        return true;
    }


    public boolean needsConfirmation(ActionContext<Y2YStreamConfigurationContainerModel> ctx)
    {
        return false;
    }


    public String getConfirmationMessage(ActionContext<Y2YStreamConfigurationContainerModel> ctx)
    {
        return null;
    }
}
