package de.hybris.platform.platformbackoffice.actions.catalogsynchronization;

import com.hybris.backoffice.sync.facades.SynchronizationFacade;
import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.engine.impl.AbstractComponentWidgetAdapterAware;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.SyncItemJobModel;
import de.hybris.platform.cronjob.enums.JobLogLevel;
import de.hybris.platform.platformbackoffice.widgets.catalogsynchronization.StartSyncForm;
import java.util.List;
import javax.annotation.Resource;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CatalogSynchronizationAction extends AbstractComponentWidgetAdapterAware implements CockpitAction<CatalogVersionModel, Object>
{
    private static final Logger LOG = LoggerFactory.getLogger(CatalogSynchronizationAction.class);
    @Resource
    private SynchronizationFacade synchronizationFacade;


    public ActionResult<Object> perform(ActionContext<CatalogVersionModel> ctx)
    {
        ActionResult<Object> result = null;
        CatalogVersionModel catVer = (CatalogVersionModel)ctx.getData();
        if(catVer != null)
        {
            LOG.debug("Collecting data for Catalog synchronization for catalog version: {}:{}", catVer.getCatalog(), catVer
                            .getVersion());
            StartSyncForm syncForm = new StartSyncForm();
            syncForm.setSourceCatalogVersion(catVer);
            syncForm.setSyncItemJobs(catVer.getSynchronizations());
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


    public boolean canPerform(ActionContext<CatalogVersionModel> ctx)
    {
        CatalogVersionModel catVer = (CatalogVersionModel)ctx.getData();
        if(catVer == null)
        {
            return false;
        }
        List<SyncItemJobModel> synchronizations = catVer.getSynchronizations();
        return (CollectionUtils.isNotEmpty(synchronizations) && synchronizations.stream().anyMatch(sync -> getSynchronizationFacade().canSync(sync)));
    }


    public boolean needsConfirmation(ActionContext<CatalogVersionModel> ctx)
    {
        return false;
    }


    public String getConfirmationMessage(ActionContext<CatalogVersionModel> ctx)
    {
        return ctx.getLabel("synchronization.confirm");
    }


    public SynchronizationFacade getSynchronizationFacade()
    {
        return this.synchronizationFacade;
    }


    public void setSynchronizationFacade(SynchronizationFacade synchronizationFacade)
    {
        this.synchronizationFacade = synchronizationFacade;
    }
}
