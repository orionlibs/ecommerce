package de.hybris.platform.cronjob.jalo;

import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.JaloSession;

public class CronJobProgressTracker extends AbstractProgressTracker
{
    private final String jobCode;
    private final String cronJobCode;
    private final PK cronJobPK;


    public CronJobProgressTracker(CronJob cronJob)
    {
        this.cronJobCode = cronJob.getCode();
        this.jobCode = cronJob.getJob().getCode();
        this.cronJobPK = cronJob.getPK();
    }


    public String getCronJobCode()
    {
        return this.cronJobCode;
    }


    public String getJobCode()
    {
        return this.jobCode;
    }


    protected void storeCronJobHistoryInDB(CronJobHistory history, Double progress)
    {
        if(history != null)
        {
            history.setProgress(progress);
            history.setStatus(history.getCronJob().getStatus());
        }
    }


    void storeProgressInDB(Double progress)
    {
        storeCronJobHistoryInDB(((CronJob)JaloSession.getCurrentSession().getItem(this.cronJobPK)).getActiveCronJobHistory(), progress);
    }
}
