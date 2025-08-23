package de.hybris.platform.servicelayer.impex.impl;

import de.hybris.platform.core.Registry;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.impex.model.ImpExMediaModel;
import de.hybris.platform.impex.model.cronjob.ImpExExportCronJobModel;
import de.hybris.platform.impex.model.exp.ExportModel;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.impex.ExportResult;

public class ExportCronJobResult implements ExportResult
{
    private final ImpExExportCronJobModel cronJob;
    private static final String BEAN_CRONJOBSERVICE = "cronJobService";


    public ExportCronJobResult(ImpExExportCronJobModel cronJob)
    {
        this.cronJob = cronJob;
    }


    public ExportModel getExport()
    {
        if(this.cronJob != null)
        {
            return this.cronJob.getExport();
        }
        return null;
    }


    public ImpExMediaModel getExportedData()
    {
        if(getExport() != null)
        {
            return (ImpExMediaModel)getExport().getExportedData();
        }
        return null;
    }


    public ImpExMediaModel getExportedMedia()
    {
        if(getExport() != null)
        {
            return (ImpExMediaModel)getExport().getExportedMedias();
        }
        return null;
    }


    public boolean isError()
    {
        return (this.cronJob != null && getCronJobService().isError((CronJobModel)this.cronJob));
    }


    public boolean isFinished()
    {
        return (this.cronJob == null || getCronJobService().isFinished((CronJobModel)this.cronJob));
    }


    public boolean isSuccessful()
    {
        return (this.cronJob == null || getCronJobService().isSuccessful((CronJobModel)this.cronJob));
    }


    public boolean isRunning()
    {
        return (this.cronJob != null && getCronJobService().isRunning((CronJobModel)this.cronJob));
    }


    public CronJobModel getCronJob()
    {
        return (CronJobModel)this.cronJob;
    }


    private CronJobService getCronJobService()
    {
        return (CronJobService)Registry.getApplicationContext().getBean("cronJobService");
    }
}
