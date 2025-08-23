package de.hybris.platform.platformbackoffice.widgets.catalogsynchronization;

import com.hybris.cockpitng.config.jaxb.wizard.CustomType;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandler;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandlerAdapter;
import de.hybris.platform.catalog.impl.CatalogUtils;
import de.hybris.platform.catalog.model.SyncItemJobModel;
import de.hybris.platform.catalog.synchronization.CatalogSynchronizationService;
import de.hybris.platform.catalog.synchronization.SyncConfig;
import de.hybris.platform.catalog.synchronization.SyncResult;
import de.hybris.platform.cronjob.enums.ErrorMode;
import de.hybris.platform.cronjob.enums.JobLogLevel;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class StartSyncWizardHandler implements FlowActionHandler
{
    private static final Logger LOG = LoggerFactory.getLogger(StartSyncWizardHandler.class);
    private CatalogSynchronizationService catalogSynchronizationService;


    public void perform(CustomType customType, FlowActionHandlerAdapter adapter, Map<String, String> parameters)
    {
        Map<String, Object> currentContext = (Map<String, Object>)adapter.getWidgetInstanceManager().getModel().getValue("currentContext", Map.class);
        SyncItemJobModel selectedSyncItemJob = (SyncItemJobModel)currentContext.get("selectedSyncItemJob");
        LOG.debug("Starting synchronization: {} -> {}", CatalogUtils.getFullCatalogVersionName(selectedSyncItemJob.getSourceVersion()),
                        CatalogUtils.getFullCatalogVersionName(selectedSyncItemJob.getTargetVersion()));
        SyncConfig syncConfig = prepareSyncConfig(currentContext);
        currentContext.put("syncResult", startSync(selectedSyncItemJob, syncConfig));
        adapter.custom();
    }


    private SyncResult startSync(SyncItemJobModel syncJob, SyncConfig syncConfig)
    {
        return this.catalogSynchronizationService.synchronize(syncJob, syncConfig);
    }


    private SyncConfig prepareSyncConfig(Map<String, Object> ctx)
    {
        SyncConfig config = new SyncConfig();
        config.setCreateSavedValues((Boolean)ctx.get("createSavedValues"));
        config.setLogToDatabase((Boolean)ctx.get("logToDatabase"));
        config.setLogToFile((Boolean)ctx.get("logToFile"));
        config.setLogLevelDatabase((JobLogLevel)ctx.get("logLevelDatabase"));
        config.setLogLevelFile((JobLogLevel)ctx.get("logLevelFile"));
        config.setForceUpdate((Boolean)ctx.get("forceUpdate"));
        Boolean ignoreErrors = (Boolean)ctx.get("ignoreErrors");
        config.setErrorMode(ignoreErrors.booleanValue() ? ErrorMode.IGNORE : ErrorMode.FAIL);
        Boolean runInBackground = (Boolean)ctx.get("runInBackground");
        config.setSynchronous(Boolean.valueOf(Boolean.FALSE.equals(runInBackground)));
        return config;
    }


    @Required
    public void setCatalogSynchronizationService(CatalogSynchronizationService catalogSynchronizationService)
    {
        this.catalogSynchronizationService = catalogSynchronizationService;
    }
}
