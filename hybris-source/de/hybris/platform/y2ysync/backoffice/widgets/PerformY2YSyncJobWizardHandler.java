package de.hybris.platform.y2ysync.backoffice.widgets;

import com.hybris.cockpitng.config.jaxb.wizard.CustomType;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandler;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandlerAdapter;
import de.hybris.y2ysync.model.Y2YSyncCronJobModel;
import de.hybris.y2ysync.model.Y2YSyncJobModel;
import de.hybris.y2ysync.services.SyncExecutionService;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class PerformY2YSyncJobWizardHandler implements FlowActionHandler
{
    private static final Logger LOG = Logger.getLogger(PerformY2YSyncJobWizardHandler.class);
    private SyncExecutionService syncExecutionService;


    public void perform(CustomType customType, FlowActionHandlerAdapter adapter, Map<String, String> map)
    {
        Map<String, Object> currentContext = (Map<String, Object>)adapter.getWidgetInstanceManager().getModel().getValue("currentContext", Map.class);
        Y2YSyncJobModel syncJob = (Y2YSyncJobModel)currentContext.get("syncJob");
        if(syncJob != null)
        {
            boolean async = ((Boolean)currentContext.get("async")).booleanValue();
            SyncExecutionService.ExecutionMode executionMode = async ? SyncExecutionService.ExecutionMode.ASYNC : SyncExecutionService.ExecutionMode.SYNC;
            LOG.info("Performing Y2YSyncJob " + syncJob.getCode() + " : " + executionMode);
            Y2YSyncCronJobModel y2YSyncCronJob = this.syncExecutionService.startSync(syncJob, executionMode);
            currentContext.put("syncCronJob", y2YSyncCronJob);
            adapter.getWidgetInstanceManager().getModel().changed();
        }
        adapter.next();
    }


    @Required
    public void setSyncExecutionService(SyncExecutionService syncExecutionService)
    {
        this.syncExecutionService = syncExecutionService;
    }
}
