package de.hybris.platform.servicelayer.cronjob.impl;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobHistoryModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.cronjob.CronJobDao;
import de.hybris.platform.servicelayer.cronjob.CronJobHistoryDao;
import de.hybris.platform.servicelayer.cronjob.CronJobHistoryInclude;
import de.hybris.platform.servicelayer.cronjob.CronJobHistoryService;
import de.hybris.platform.servicelayer.internal.service.AbstractBusinessService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCronJobHistoryService extends AbstractBusinessService implements CronJobHistoryService
{
    private static final int CRONJOB_HISTORY_LIMIT = 1000;
    private CronJobHistoryDao cronJobHistoryDao;
    private CronJobDao cronJobDao;


    public List<CronJobHistoryModel> getCronJobHistoryBy(String cronJobCode)
    {
        ServicesUtil.validateParameterNotNull(cronJobCode, "Parameter cronJobCode must not be null");
        return this.cronJobHistoryDao.findCronJobHistoryBy(cronJobCode);
    }


    public List<CronJobHistoryModel> getCronJobHistoryBy(List<String> cronJobCodes)
    {
        ServicesUtil.validateParameterNotNull(cronJobCodes, "Parameter cronJobCodes must not be null");
        return this.cronJobHistoryDao.findCronJobHistoryBy(cronJobCodes);
    }


    public List<CronJobHistoryModel> getCronJobHistoryBy(@Nullable UserModel userModel, JobModel jobModel)
    {
        ServicesUtil.validateParameterNotNull(jobModel, "Parameter jobModel must not be null");
        return this.cronJobHistoryDao.findCronJobHistoryBy((userModel != null) ? userModel.getUid() : null, jobModel.getCode());
    }


    public List<CronJobHistoryModel> getCronJobHistoryBy(UserModel userModel, String jobItemType, Date startDate, Date finishDate)
    {
        String userUid = (userModel != null) ? userModel.getUid() : null;
        return this.cronJobHistoryDao.findCronJobHistoryBy(userUid, jobItemType, startDate, finishDate);
    }


    public List<CronJobHistoryModel> getCronJobHistoryBy(UserModel userModel, String jobItemType, Date startDate, Date finishDate, CronJobStatus theStatus)
    {
        String userUid = (userModel != null) ? userModel.getUid() : null;
        return this.cronJobHistoryDao.findCronJobHistoryBy(userUid, jobItemType, startDate, finishDate, theStatus);
    }


    public List<CronJobHistoryModel> getCronJobHistoryBy(UserModel userModel, String jobItemType, Date startDate, Date finishDate, CronJobResult theResult)
    {
        String userUid = (userModel != null) ? userModel.getUid() : null;
        return this.cronJobHistoryDao.findCronJobHistoryBy(userUid, jobItemType, startDate, finishDate, theResult);
    }


    public List<CronJobHistoryModel> getCronJobHistoryBy(Set<CronJobHistoryInclude> includes, UserModel userModel, Date startDate, Date finishDate, CronJobStatus theStatus)
    {
        String userUid = (userModel != null) ? userModel.getUid() : null;
        return this.cronJobHistoryDao.findCronJobHistoryBy(includes, userUid, startDate, finishDate, theStatus, null);
    }


    public Long getAverageExecutionTime(UserModel userModel, String jobCode, TimeUnit timeUnit)
    {
        ServicesUtil.validateParameterNotNull(userModel, "Parameter userModel must not be null");
        ServicesUtil.validateParameterNotNull(timeUnit, "Parameter timeUnit must not be null");
        return calculateAverageTimeFor(timeUnit, this.cronJobHistoryDao.findCronJobHistoryBy(userModel.getUid(), jobCode));
    }


    public Long getAverageExecutionTime(CronJobModel cronJob, TimeUnit timeUnit)
    {
        ServicesUtil.validateParameterNotNull(cronJob, "Parameter cronJob must not be null");
        ServicesUtil.validateParameterNotNull(timeUnit, "Parameter timeUnit must not be null");
        List<CronJobModel> allCronJobsWithJob = this.cronJobDao.findLastCronJobsWithJob(cronJob.getJob(), 1000);
        return calculateAverageTimeFor(timeUnit, this.cronJobHistoryDao.findLastCronJobHistoryByCronJobs(allCronJobsWithJob, 1000));
    }


    private Long calculateAverageTimeFor(TimeUnit timeUnit, List<CronJobHistoryModel> cronJobHistoryByCronJobs)
    {
        long sum = 0L;
        int count = 0;
        for(CronJobHistoryModel entry : cronJobHistoryByCronJobs)
        {
            if(CronJobStatus.FINISHED.equals(entry.getStatus()) && (CronJobResult.SUCCESS
                            .equals(entry.getResult()) & ((entry.getEndTime() != null) ? 1 : 0)) != 0)
            {
                sum += entry.getEndTime().getTime() - entry.getStartTime().getTime();
                count++;
            }
        }
        return (count == 0) ? null : Long.valueOf(timeUnit.convert(sum / count, TimeUnit.MILLISECONDS));
    }


    @Required
    public void setCronJobHistoryDao(CronJobHistoryDao cronJobHistoryDao)
    {
        this.cronJobHistoryDao = cronJobHistoryDao;
    }


    @Required
    public void setCronJobDao(CronJobDao cronJobDao)
    {
        this.cronJobDao = cronJobDao;
    }
}
