package de.hybris.platform.servicelayer.cronjob;

import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.internal.dao.Dao;
import java.util.Collections;
import java.util.List;

public interface CronJobDao extends Dao
{
    List<CronJobModel> findCronJobs(String paramString);


    List<CronJobModel> findRunningCronJobs();


    boolean isPerformable(CronJobModel paramCronJobModel);


    boolean isAbortable(CronJobModel paramCronJobModel);


    default List<CronJobModel> findLastCronJobsWithJob(JobModel jobModel, int limit)
    {
        return Collections.emptyList();
    }
}
