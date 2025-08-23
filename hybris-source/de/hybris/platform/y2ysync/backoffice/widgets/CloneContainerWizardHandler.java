package de.hybris.platform.y2ysync.backoffice.widgets;

import com.hybris.cockpitng.config.jaxb.wizard.CustomType;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandler;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandlerAdapter;
import de.hybris.platform.util.logging.Logs;
import de.hybris.y2ysync.model.Y2YStreamConfigurationContainerModel;
import de.hybris.y2ysync.services.StreamConfigCloningService;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class CloneContainerWizardHandler implements FlowActionHandler
{
    private static final Logger LOG = Logger.getLogger(CloneContainerWizardHandler.class);
    private StreamConfigCloningService streamConfigCloningService;


    public void perform(CustomType customType, FlowActionHandlerAdapter adapter, Map<String, String> parameters)
    {
        Map<String, Object> currentContext = (Map<String, Object>)adapter.getWidgetInstanceManager().getModel().getValue("currentContext", Map.class);
        Y2YStreamConfigurationContainerModel sourceContainer = (Y2YStreamConfigurationContainerModel)currentContext.get("sourceContainer");
        String targetContainerId = (String)currentContext.get("targetContainerId");
        Y2YStreamConfigurationContainerModel targetContainer = this.streamConfigCloningService.cloneStreamContainer(sourceContainer, targetContainerId);
        Logs.debug(LOG, () -> "Cloning of Container " + sourceContainer.getId() + " successfully executed, new ContainerID: " + targetContainer.getId());
        currentContext.put("targetContainer", targetContainer);
        adapter.next();
    }


    @Required
    public void setStreamConfigCloningService(StreamConfigCloningService streamConfigCloningService)
    {
        this.streamConfigCloningService = streamConfigCloningService;
    }
}
