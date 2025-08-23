package de.hybris.platform.y2ysync.backoffice.actions;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import de.hybris.deltadetection.ChangeDetectionService;
import de.hybris.deltadetection.model.StreamConfigurationContainerModel;
import de.hybris.deltadetection.model.StreamConfigurationModel;
import javax.annotation.Resource;
import org.apache.log4j.Logger;

public class DeleteStreamAction implements CockpitAction<StreamConfigurationContainerModel, Object>
{
    @Resource
    private ChangeDetectionService changeDetectionService;
    private static final Logger LOG = Logger.getLogger(DeleteStreamAction.class);


    public ActionResult<Object> perform(ActionContext<StreamConfigurationContainerModel> ctx)
    {
        StreamConfigurationContainerModel streamConfigContainer = (StreamConfigurationContainerModel)ctx.getData();
        for(StreamConfigurationModel streamConfig : streamConfigContainer.getConfigurations())
        {
            if(LOG.isInfoEnabled())
            {
                LOG.info("Deleting all item version markers related to Stream '" + streamConfig.getStreamId() + "'");
            }
            this.changeDetectionService.deleteItemVersionMarkersForStream(streamConfig.getStreamId());
        }
        return new ActionResult("success");
    }


    public boolean canPerform(ActionContext<StreamConfigurationContainerModel> ctx)
    {
        return true;
    }


    public boolean needsConfirmation(ActionContext<StreamConfigurationContainerModel> ctx)
    {
        return true;
    }


    public String getConfirmationMessage(ActionContext<StreamConfigurationContainerModel> ctx)
    {
        return ctx.getLabel("perform.confirm");
    }
}
