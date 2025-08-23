package de.hybris.platform.platformbackoffice.actions.catalogsynchronization;

import com.hybris.backoffice.sync.facades.SynchronizationFacade;
import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.engine.impl.AbstractComponentWidgetAdapterAware;
import de.hybris.platform.catalog.model.SyncItemJobModel;
import de.hybris.platform.catalog.model.synchronization.CatalogVersionSyncJobModel;
import de.hybris.platform.cronjob.enums.JobLogLevel;
import de.hybris.platform.platformbackoffice.widgets.catalogsynchronization.StartSyncForm;
import java.util.Collections;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExecuteCatalogSynchronizationJobAction extends AbstractComponentWidgetAdapterAware implements CockpitAction<CatalogVersionSyncJobModel, Object>
{
    private static final Logger LOG = LoggerFactory.getLogger(ExecuteCatalogSynchronizationJobAction.class);
    @Resource
    private SynchronizationFacade synchronizationFacade;


    public ActionResult<Object> perform(ActionContext<CatalogVersionSyncJobModel> ctx)
    {
        ActionResult<Object> result = null;
        CatalogVersionSyncJobModel syncJob = (CatalogVersionSyncJobModel)ctx.getData();
        if(syncJob != null)
        {
            LOG.debug("Collecting data for Catalog synchronization for catalog version sync job: {}", syncJob.getCode());
            StartSyncForm syncForm = new StartSyncForm();
            syncForm.setSourceCatalogVersion(syncJob.getSourceVersion());
            syncForm.setSyncItemJobs(Collections.singletonList(syncJob));
            syncForm.setSelectedSyncItemJob((SyncItemJobModel)syncJob);
            syncForm.setLogToFile(true);
            syncForm.setLogToDatabase(false);
            syncForm.setLogLevelDatabase(JobLogLevel.WARNING);
            syncForm.setLogLevelFile(JobLogLevel.INFO);
            syncForm.setRunInBackground(true);
            syncForm.setIgnoreErrors(true);
            syncForm.setKeepCronJob(true);
            syncForm.setCreateSavedValues(false);
            syncForm.setForceUpdate(false);
            result = new ActionResult("success");
            sendOutput("startSyncForm", syncForm);
        }
        else
        {
            result = new ActionResult("error");
        }
        return result;
    }


    public boolean canPerform(ActionContext<CatalogVersionSyncJobModel> ctx)
    {
        CatalogVersionSyncJobModel syncJob = (CatalogVersionSyncJobModel)ctx.getData();
        if(syncJob == null)
        {
            return false;
        }
        return getSynchronizationFacade().canSync((SyncItemJobModel)syncJob);
    }


    public boolean needsConfirmation(ActionContext<CatalogVersionSyncJobModel> ctx)
    {
        return false;
    }


    public String getConfirmationMessage(ActionContext<CatalogVersionSyncJobModel> ctx)
    {
        return ctx.getLabel("synchronization.confirm");
    }


    protected SynchronizationFacade getSynchronizationFacade()
    {
        return this.synchronizationFacade;
    }
}
