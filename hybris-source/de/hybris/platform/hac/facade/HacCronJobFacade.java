package de.hybris.platform.hac.facade;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.hac.data.dto.CronJobData;
import de.hybris.platform.servicelayer.cronjob.CronJobHistoryService;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.util.Config;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class HacCronJobFacade
{
    private static final Logger LOG = Logger.getLogger(HacCronJobFacade.class);
    private static final int DEFAULT_DURATION = 5;
    private final Supplier<List<CronJobData>> cronJobDataSupplier;
    private CronJobService cronJobService;
    private CronJobHistoryService cronJobHistoryService;


    public HacCronJobFacade()
    {
        this(getDuration());
    }


    public HacCronJobFacade(int duration)
    {
        this.cronJobDataSupplier = (Supplier<List<CronJobData>>)Suppliers.memoizeWithExpiration(this::obtainRunningOrRestartedCronJobsData, duration, TimeUnit.SECONDS);
    }


    private static int getDuration()
    {
        return Config.getInt("hac.cronjobs.table.refresh.interval", 5);
    }


    public List<CronJobData> getRunningOrRestartedCronJobsData()
    {
        return this.cronJobDataSupplier.get();
    }


    private List<CronJobData> obtainRunningOrRestartedCronJobsData()
    {
        List<CronJobData> result = new ArrayList<>();
        try
        {
            for(CronJobModel cronJob : this.cronJobService.getRunningOrRestartedCronJobs())
            {
                result.add(new CronJobData(cronJob.getCode(), cronJob.getJob().getCode(), getStatus(cronJob)));
            }
        }
        catch(SystemException e)
        {
            logSystemException(e);
        }
        return (List<CronJobData>)ImmutableList.copyOf(result);
    }


    private String getStatus(CronJobModel cronJob)
    {
        StringBuilder builder = new StringBuilder(createProgressBar(cronJob));
        if(cronJob.getActiveCronJobHistory() != null && cronJob.getActiveCronJobHistory().getStatusLine() != null)
        {
            builder.append(" ");
            builder.append(cronJob.getActiveCronJobHistory().getStatusLine());
        }
        return builder.toString();
    }


    private String createProgressBar(Double progress, Long average, Long remaining, Long elapsed)
    {
        StringBuilder builder = new StringBuilder();
        if(progress != null)
        {
            builder.append(String.format("(%2.0f%%) ", new Object[] {progress}));
            builder.append("[");
            for(int i = 0; i < 100; i += 3)
            {
                if(i < progress.doubleValue())
                {
                    builder.append("+");
                }
                else
                {
                    builder.append("-");
                }
            }
            builder.append("] ");
        }
        builder.append("elapsed: ");
        if(elapsed == null)
        {
            builder.append("N/A");
        }
        else
        {
            builder.append(String.format("%3ds", new Object[] {elapsed}));
        }
        builder.append(", average: ");
        if(average == null)
        {
            builder.append("N/A");
        }
        else
        {
            builder.append(String.format("%3ds", new Object[] {average}));
        }
        if(remaining == null)
        {
            builder.append(", remaining: N/A");
        }
        else if(remaining.longValue() < 0L)
        {
            builder.append(String.format(", %3ds slower than usual%n", new Object[] {Long.valueOf(Math.abs(remaining.longValue()))}));
        }
        else
        {
            builder.append(String.format(", remaining: %3ds%n", new Object[] {remaining}));
        }
        return builder.toString();
    }


    private String createProgressBar(CronJobModel cronJob)
    {
        if(cronJob.getActiveCronJobHistory() != null)
        {
            Double progress = cronJob.getActiveCronJobHistory().getProgress();
            Long average = this.cronJobHistoryService.getAverageExecutionTime(cronJob, TimeUnit.SECONDS);
            Long elapsed = Long.valueOf(((new Date()).getTime() - cronJob.getStartTime().getTime()) / 1000L);
            Long remaining = null;
            if(average != null)
            {
                remaining = Long.valueOf(average.longValue() - elapsed.longValue());
            }
            return createProgressBar(progress, average, remaining, elapsed);
        }
        return "No data available.";
    }


    public Map<String, Boolean> requestAbortForRunningCronJobs()
    {
        Map<String, Boolean> result = new HashMap<>();
        try
        {
            for(CronJobModel cronJob : this.cronJobService.getRunningOrRestartedCronJobs())
            {
                if(this.cronJobService.isRunning(cronJob))
                {
                    try
                    {
                        this.cronJobService.requestAbortCronJob(cronJob);
                        result.put(cronJob.getCode(), Boolean.TRUE);
                    }
                    catch(IllegalStateException e)
                    {
                        result.put(cronJob.getCode(), Boolean.FALSE);
                        String msg = "Cannot abort cron job (reason: " + e.getMessage() + ")";
                        LOG.warn(msg);
                        if(LOG.isDebugEnabled())
                        {
                            LOG.debug(msg, e);
                        }
                    }
                }
            }
        }
        catch(SystemException e)
        {
            logSystemException(e);
        }
        return result;
    }


    private void logSystemException(SystemException e)
    {
        String message = "Cannot obtain list of running CronJobs (reason: " + e.getMessage() + ")";
        LOG.error(message);
        if(LOG.isDebugEnabled())
        {
            LOG.debug(message, (Throwable)e);
        }
    }


    @Required
    public void setCronJobService(CronJobService cronJobService)
    {
        this.cronJobService = cronJobService;
    }


    @Required
    public void setCronJobHistoryService(CronJobHistoryService cronJobHistoryService)
    {
        this.cronJobHistoryService = cronJobHistoryService;
    }
}
