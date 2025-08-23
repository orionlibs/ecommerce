package de.hybris.platform.servicelayer.cronjob;

import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobLogModel;
import java.util.List;

public interface JobLogDao
{
    List<JobLogModel> findJobLogs(CronJobModel paramCronJobModel, int paramInt, boolean paramBoolean);
}
