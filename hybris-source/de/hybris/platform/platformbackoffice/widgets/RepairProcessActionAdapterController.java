package de.hybris.platform.platformbackoffice.widgets;

import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.cockpitng.widgets.configurableflow.ConfigurableFlowContextParameterNames;
import de.hybris.platform.platformbackoffice.widgets.processengine.RepairProcessForm;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RepairProcessActionAdapterController extends DefaultWidgetController
{
    public static final Logger LOG = LoggerFactory.getLogger(RepairProcessActionAdapterController.class);
    public static final String SOCKET_IN_START_SYNC_FORM = "repairProcessForm";
    public static final String SOCKET_OUT_CONTEXT_MAP = "contextMap";


    @SocketEvent(socketId = "repairProcessForm")
    public void adjustForSyncWizard(RepairProcessForm repairProcessForm)
    {
        if(repairProcessForm == null)
        {
            LOG.warn("input object for repair business process wizard is null");
            return;
        }
        Map<String, Object> contextMap = new HashMap<>();
        contextMap.put("businessProcess", repairProcessForm.getBusinessProcess());
        contextMap.put("targetStep", repairProcessForm.getTargetStep());
        contextMap.put("successful", Boolean.valueOf(repairProcessForm.isSuccessful()));
        contextMap.put(ConfigurableFlowContextParameterNames.TYPE_CODE.getName(), repairProcessForm.getClass().getName());
        contextMap.put(ConfigurableFlowContextParameterNames.PARENT_OBJECT.getName(), repairProcessForm);
        sendOutput("contextMap", contextMap);
    }
}
