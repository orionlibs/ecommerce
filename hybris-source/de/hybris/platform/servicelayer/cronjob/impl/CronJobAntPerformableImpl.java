package de.hybris.platform.servicelayer.cronjob.impl;

import de.hybris.ant.taskdefs.AbstractAntPerformable;
import de.hybris.platform.core.Registry;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobLogModel;
import org.apache.log4j.Logger;

public class CronJobAntPerformableImpl extends AbstractAntPerformable
{
    private static final Logger LOG = Logger.getLogger(CronJobAntPerformableImpl.class.getName());
    private final String cronJobId;
    private DefaultCronJobService service;


    public CronJobAntPerformableImpl(String cronjobCode)
    {
        this(cronjobCode, Registry.getMasterTenant().getTenantID());
    }


    public CronJobAntPerformableImpl(String cronjobCode, String currentTennantId)
    {
        super(currentTennantId);
        LOG.info("Performing cron job " + cronjobCode);
        this.cronJobId = cronjobCode;
    }


    public boolean validate()
    {
        try
        {
            this.service = (DefaultCronJobService)getApplicationContext().getBean("cronJobService");
            if(this.service.getCronJob(this.cronJobId) == null)
            {
                LOG.info("Cannot find cron job speciafied by code as :" + this.cronJobId + " for tennant: " + this.tenantId);
                return false;
            }
        }
        catch(Exception e)
        {
            LOG.error(e);
            return false;
        }
        return true;
    }


    protected void performImpl() throws Exception
    {
        CronJobStatus status = null;
        try
        {
            CronJobModel cronJobModel = this.service.getCronJob(this.cronJobId);
            this.service.performCronJob(cronJobModel, true);
            for(JobLogModel jlm : cronJobModel.getLogs())
            {
                LOG.info(jlm.getShortMessage());
            }
            LOG.info("Log text :" + cronJobModel.getLogText());
            status = cronJobModel.getStatus();
        }
        finally
        {
            if(status != null)
            {
                LOG.info("Cronjob status after execution :" + status);
            }
        }
    }
}
