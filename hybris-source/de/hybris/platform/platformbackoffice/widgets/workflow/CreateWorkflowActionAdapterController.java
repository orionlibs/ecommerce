package de.hybris.platform.platformbackoffice.widgets.workflow;

import com.hybris.backoffice.workflow.wizard.CollaborationWorkflowWizardForm;
import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.cockpitng.widgets.configurableflow.ConfigurableFlowContextParameterNames;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateWorkflowActionAdapterController extends DefaultWidgetController
{
    public static final Logger LOG = LoggerFactory.getLogger(CreateWorkflowActionAdapterController.class);
    public static final String SOCKET_IN = "createWorkflowForm";
    public static final String SOCKET_OUT_CONTEXT_MAP = "contextMap";


    @SocketEvent(socketId = "createWorkflowForm")
    public void adjustForSyncWizard(CreateWorkflowForm createWorkflowForm)
    {
        if(createWorkflowForm == null)
        {
            LOG.warn("input object for create workflow is null");
            return;
        }
        Map<String, Object> contextMap = new HashMap<>();
        contextMap.put("workflowTemplate", createWorkflowForm.getWorkflowTemplate());
        contextMap.put(ConfigurableFlowContextParameterNames.TYPE_CODE.getName(), CollaborationWorkflowWizardForm.class.getName());
        sendOutput("contextMap", contextMap);
    }
}
