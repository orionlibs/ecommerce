package de.hybris.platform.platformbackoffice.widgets;

import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.cockpitng.widgets.configurableflow.ConfigurableFlowContextParameterNames;
import de.hybris.platform.platformbackoffice.widgets.catalogsynchronization.StartSyncForm;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SyncActionAdapterController extends DefaultWidgetController
{
    public static final Logger LOG = LoggerFactory.getLogger(SyncActionAdapterController.class);
    public static final String SOCKET_IN_START_SYNC_FORM = "startSyncForm";
    public static final String SOCKET_OUT_CONTEXT_MAP = "contextMap";


    @SocketEvent(socketId = "startSyncForm")
    public void adjustForSyncWizard(StartSyncForm startSyncForm)
    {
        if(startSyncForm == null)
        {
            LOG.warn("input object for sync wizard is null");
            return;
        }
        Map<String, Object> contextMap = new HashMap<>();
        contextMap.put("sourceCatalogVersion", startSyncForm.getSourceCatalogVersion());
        contextMap.put("forceUpdate", Boolean.valueOf(startSyncForm.isForceUpdate()));
        contextMap.put("keepCronJob", Boolean.valueOf(startSyncForm.isKeepCronJob()));
        contextMap.put("logLevelDatabase", startSyncForm.getLogLevelDatabase());
        contextMap.put("logLevelFile", startSyncForm.getLogLevelFile());
        contextMap.put("logToFile", Boolean.valueOf(startSyncForm.isLogToFile()));
        contextMap.put("logToDatabase", Boolean.valueOf(startSyncForm.isLogToDatabase()));
        contextMap.put("ignoreErrors", Boolean.valueOf(startSyncForm.isIgnoreErrors()));
        contextMap.put("createSavedValues", Boolean.valueOf(startSyncForm.isCreateSavedValues()));
        contextMap.put("syncItemJobs", startSyncForm.getSyncItemJobs());
        contextMap.put("runInBackground", Boolean.valueOf(startSyncForm.isRunInBackground()));
        contextMap.put("selectedSyncItemJob", startSyncForm.getSelectedSyncItemJob());
        contextMap.put(ConfigurableFlowContextParameterNames.TYPE_CODE.getName(), startSyncForm.getClass().getName());
        contextMap.put(ConfigurableFlowContextParameterNames.PARENT_OBJECT.getName(), startSyncForm);
        sendOutput("contextMap", contextMap);
    }
}
