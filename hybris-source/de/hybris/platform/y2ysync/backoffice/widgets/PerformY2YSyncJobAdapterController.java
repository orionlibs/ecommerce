package de.hybris.platform.y2ysync.backoffice.widgets;

import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.cockpitng.widgets.configurableflow.ConfigurableFlowContextParameterNames;
import de.hybris.platform.y2ysync.backoffice.data.Y2YSyncJobForm;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PerformY2YSyncJobAdapterController extends DefaultWidgetController
{
    private static final Logger LOG = LoggerFactory.getLogger(PerformY2YSyncJobAdapterController.class);
    public static final String SOCKET_IN_PERFORM_Y2Y_SYNC_JOB = "performY2YSyncJob";
    public static final String SOCKET_OUT_CLONE_CONTAINER_CONTEXT_MAP = "contextMap";


    @SocketEvent(socketId = "performY2YSyncJob")
    public void adjustForCloneContainerWizard(Y2YSyncJobForm y2ySyncJobForm)
    {
        if(y2ySyncJobForm == null)
        {
            LOG.warn("input object for clone stream container wizard is null");
            return;
        }
        Map<String, Object> contextMap = new HashMap<>();
        contextMap.put("async", Boolean.valueOf(y2ySyncJobForm.isAsync()));
        contextMap.put("syncJob", y2ySyncJobForm.getSyncJob());
        contextMap.put("syncCronJob", y2ySyncJobForm.getSyncCronJob());
        contextMap.put(ConfigurableFlowContextParameterNames.TYPE_CODE.getName(), y2ySyncJobForm.getClass().getName());
        contextMap.put(ConfigurableFlowContextParameterNames.PARENT_OBJECT.getName(), y2ySyncJobForm);
        sendOutput("contextMap", contextMap);
    }
}
