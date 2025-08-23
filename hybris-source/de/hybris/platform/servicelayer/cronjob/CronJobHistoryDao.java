package de.hybris.platform.servicelayer.cronjob;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobHistoryModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public interface CronJobHistoryDao
{
    List<CronJobHistoryModel> findCronJobHistoryBy(String paramString);


    List<CronJobHistoryModel> findCronJobHistoryBy(List<String> paramList);


    default List<CronJobHistoryModel> findLastCronJobHistoryByCronJobs(List<CronJobModel> cronJobs, int limit)
    {
        List<String> cronJobCodes = (List<String>)cronJobs.stream().map(CronJobModel::getCode).collect(Collectors.toList());
        return findCronJobHistoryBy(cronJobCodes);
    }


    List<CronJobHistoryModel> findCronJobHistoryBy(String paramString1, String paramString2);


    List<CronJobHistoryModel> findCronJobHistoryBy(String paramString1, String paramString2, Date paramDate1, Date paramDate2);


    List<CronJobHistoryModel> findCronJobHistoryBy(String paramString1, String paramString2, Date paramDate1, Date paramDate2, CronJobStatus paramCronJobStatus);


    List<CronJobHistoryModel> findCronJobHistoryBy(String paramString1, String paramString2, Date paramDate1, Date paramDate2, CronJobResult paramCronJobResult);


    List<CronJobHistoryModel> findCronJobHistoryBy(Set<CronJobHistoryInclude> paramSet, String paramString, Date paramDate1, Date paramDate2, CronJobStatus paramCronJobStatus, CronJobResult paramCronJobResult);
}
