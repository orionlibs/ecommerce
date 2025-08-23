package de.hybris.platform.servicelayer.event.events;

import de.hybris.platform.core.PK;
import java.io.Serializable;

public abstract class AbstractCronJobEvent extends AbstractEvent
{
    private PK cronJobPK;
    private String cronJob;
    private String cronJobType;
    private String job;
    private String jobType;


    public AbstractCronJobEvent()
    {
    }


    public AbstractCronJobEvent(Serializable source)
    {
        super(source);
    }


    public void setCronJobPK(PK cronJobPK)
    {
        this.cronJobPK = cronJobPK;
    }


    public PK getCronJobPK()
    {
        return this.cronJobPK;
    }


    public void setCronJob(String cronJob)
    {
        this.cronJob = cronJob;
    }


    public String getCronJob()
    {
        return this.cronJob;
    }


    public void setCronJobType(String cronJobType)
    {
        this.cronJobType = cronJobType;
    }


    public String getCronJobType()
    {
        return this.cronJobType;
    }


    public void setJob(String job)
    {
        this.job = job;
    }


    public String getJob()
    {
        return this.job;
    }


    public void setJobType(String jobType)
    {
        this.jobType = jobType;
    }


    public String getJobType()
    {
        return this.jobType;
    }
}
