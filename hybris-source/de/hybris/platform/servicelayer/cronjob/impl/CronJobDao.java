package de.hybris.platform.servicelayer.cronjob.impl;

import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.internal.dao.Dao;
import java.util.List;

@Deprecated(since = "ages", forRemoval = true)
public interface CronJobDao extends Dao
{
    List<CronJobModel> findCronJobs(String paramString);


    @Deprecated(since = "ages", forRemoval = true)
    List<JobModel> findJobs(String paramString);


    List<CronJobModel> findRunningCronJobs();


    boolean findPerformable(CronJobModel paramCronJobModel);


    boolean findAbortable(CronJobModel paramCronJobModel);


    boolean findUndoable(CronJobModel paramCronJobModel);
}
