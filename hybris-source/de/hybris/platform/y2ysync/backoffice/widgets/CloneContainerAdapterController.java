package de.hybris.platform.y2ysync.backoffice.widgets;

import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.cockpitng.widgets.configurableflow.ConfigurableFlowContextParameterNames;
import de.hybris.platform.y2ysync.backoffice.data.Y2YCloneContainerForm;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CloneContainerAdapterController extends DefaultWidgetController
{
    private static final Logger LOG = LoggerFactory.getLogger(CloneContainerAdapterController.class);
    public static final String SOCKET_IN_CLONE_CONTAINER_FORM = "cloneContainerForm";
    public static final String SOCKET_OUT_CLONE_CONTAINER_CONTEXT_MAP = "contextMap";


    @SocketEvent(socketId = "cloneContainerForm")
    public void adjustForCloneContainerWizard(Y2YCloneContainerForm y2yCloneContainerForm)
    {
        if(y2yCloneContainerForm == null)
        {
            LOG.warn("input object for clone stream container wizard is null");
            return;
        }
        Map<String, Object> contextMap = new HashMap<>();
        contextMap.put("sourceContainer", y2yCloneContainerForm.getSourceContainer());
        contextMap.put("targetContainerId", y2yCloneContainerForm.getTargetContainerId());
        contextMap.put("targetContainer", y2yCloneContainerForm.getTargetContainer());
        contextMap.put(ConfigurableFlowContextParameterNames.TYPE_CODE.getName(), y2yCloneContainerForm.getClass().getName());
        contextMap.put(ConfigurableFlowContextParameterNames.PARENT_OBJECT.getName(), y2yCloneContainerForm);
        sendOutput("contextMap", contextMap);
    }
}
