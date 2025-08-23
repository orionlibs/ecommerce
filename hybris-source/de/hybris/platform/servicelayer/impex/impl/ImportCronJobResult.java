package de.hybris.platform.servicelayer.impex.impl;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.impex.model.ImpExMediaModel;
import de.hybris.platform.impex.model.cronjob.ImpExImportCronJobModel;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.impex.ImportResult;
import de.hybris.platform.servicelayer.model.ModelService;

public class ImportCronJobResult implements ImportResult
{
    private final PK cronJobPK;
    private static final String BEAN_CRONJOBSERVICE = "cronJobService";
    private static final String BEAN_MODELSERVICE = "modelService";


    public ImportCronJobResult(ImpExImportCronJobModel cronJob)
    {
        this.cronJobPK = (cronJob != null) ? cronJob.getPk() : null;
    }


    public ImpExMediaModel getUnresolvedLines()
    {
        if(this.cronJobPK != null)
        {
            return getCronJob().getUnresolvedDataStore();
        }
        return null;
    }


    public boolean hasUnresolvedLines()
    {
        if(this.cronJobPK != null)
        {
            return (getCronJob().getUnresolvedDataStore() != null);
        }
        return false;
    }


    public boolean isError()
    {
        return (this.cronJobPK != null && getCronJobService().isError((CronJobModel)getCronJob()));
    }


    public boolean isFinished()
    {
        return (this.cronJobPK == null || getCronJobService().isFinished((CronJobModel)getCronJob()));
    }


    public boolean isSuccessful()
    {
        return (this.cronJobPK == null || getCronJobService().isSuccessful((CronJobModel)getCronJob()));
    }


    public boolean isRunning()
    {
        return (this.cronJobPK != null && getCronJobService().isRunning((CronJobModel)getCronJob()));
    }


    private CronJobService getCronJobService()
    {
        return (CronJobService)Registry.getApplicationContext().getBean("cronJobService", CronJobService.class);
    }


    private ModelService getModelService()
    {
        return (ModelService)Registry.getApplicationContext().getBean("modelService", ModelService.class);
    }


    public ImpExImportCronJobModel getCronJob()
    {
        if(this.cronJobPK == null)
        {
            return null;
        }
        ModelService modelService = getModelService();
        ImpExImportCronJobModel cronJob = (ImpExImportCronJobModel)modelService.get(this.cronJobPK);
        modelService.refresh(cronJob);
        return cronJob;
    }
}
