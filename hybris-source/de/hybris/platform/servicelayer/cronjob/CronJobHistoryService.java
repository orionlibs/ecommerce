package de.hybris.platform.servicelayer.cronjob;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobHistoryModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public interface CronJobHistoryService
{
    List<CronJobHistoryModel> getCronJobHistoryBy(String paramString);


    List<CronJobHistoryModel> getCronJobHistoryBy(List<String> paramList);


    List<CronJobHistoryModel> getCronJobHistoryBy(UserModel paramUserModel, JobModel paramJobModel);


    List<CronJobHistoryModel> getCronJobHistoryBy(UserModel paramUserModel, String paramString, Date paramDate1, Date paramDate2);


    List<CronJobHistoryModel> getCronJobHistoryBy(UserModel paramUserModel, String paramString, Date paramDate1, Date paramDate2, CronJobStatus paramCronJobStatus);


    List<CronJobHistoryModel> getCronJobHistoryBy(Set<CronJobHistoryInclude> paramSet, UserModel paramUserModel, Date paramDate1, Date paramDate2, CronJobStatus paramCronJobStatus);


    List<CronJobHistoryModel> getCronJobHistoryBy(UserModel paramUserModel, String paramString, Date paramDate1, Date paramDate2, CronJobResult paramCronJobResult);


    Long getAverageExecutionTime(UserModel paramUserModel, String paramString, TimeUnit paramTimeUnit);


    default Long getAverageExecutionTime(CronJobModel cronJob, TimeUnit timeUnit)
    {
        return getAverageExecutionTime(cronJob.getSessionUser(), cronJob.getJob().getCode(), timeUnit);
    }
}
