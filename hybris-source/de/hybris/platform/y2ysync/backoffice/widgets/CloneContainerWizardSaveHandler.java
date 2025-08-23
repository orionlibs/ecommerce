package de.hybris.platform.y2ysync.backoffice.widgets;

import com.hybris.cockpitng.config.jaxb.wizard.CustomType;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandler;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandlerAdapter;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.y2ysync.model.Y2YStreamConfigurationContainerModel;
import de.hybris.y2ysync.services.StreamConfigCloningService;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class CloneContainerWizardSaveHandler implements FlowActionHandler
{
    private static final Logger LOG = Logger.getLogger(CloneContainerWizardSaveHandler.class);
    private ModelService modelService;


    public void perform(CustomType customType, FlowActionHandlerAdapter adapter, Map<String, String> parameters)
    {
        Map<String, Object> currentContext = (Map<String, Object>)adapter.getWidgetInstanceManager().getModel().getValue("currentContext", Map.class);
        Y2YStreamConfigurationContainerModel targetContainer = (Y2YStreamConfigurationContainerModel)currentContext.get("targetContainer");
        if(targetContainer != null)
        {
            LOG.info("Saving cloned container " + targetContainer.getId());
            this.modelService.save(targetContainer);
        }
        adapter.done();
    }


    @Deprecated
    public void setStreamConfigCloningService(StreamConfigCloningService streamConfigCloningService)
    {
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }
}
