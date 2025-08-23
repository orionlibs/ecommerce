package de.hybris.platform.cronjob.mbeans;

import de.hybris.platform.cronjob.mbeans.event.CronJobInfoEvent;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.util.Config;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MetricsCronJobHolder
{
    protected static final String METRICS_CRON_JOBS_RUNNING_CACHE_TTL_MS = "metrics.cronJobsRunning.cache.ttl.ms";
    protected static final int TTL_MS = 10000;
    private static final Logger LOG = LoggerFactory.getLogger(MetricsCronJobHolder.class);
    private final AtomicReference<CronJobMetrics> cronJobMetrics = new AtomicReference<>(new CronJobMetrics(Instant.EPOCH,
                    List.of()));
    public final Duration duration;
    private final CronJobService cronJobService;
    private final EventService eventService;


    public MetricsCronJobHolder(CronJobService cronJobService, EventService eventService)
    {
        this.cronJobService = Objects.<CronJobService>requireNonNull(cronJobService);
        this.eventService = Objects.<EventService>requireNonNull(eventService);
        this.duration = Duration.ofMillis(Config.getLong("metrics.cronJobsRunning.cache.ttl.ms", 10000L));
    }


    public void cacheCronJobMetrics(Instant timeInstant, Collection<String> cronJobList)
    {
        this.cronJobMetrics.set(new CronJobMetrics(timeInstant, cronJobList));
        LOG.debug("Updating cache cronJobs List");
    }


    public Collection<String> getRunningCronJobs()
    {
        CronJobMetrics oldMetrics = this.cronJobMetrics.get();
        if(!isCacheExpired(oldMetrics))
        {
            LOG.debug("Return cached cronJob list");
            return oldMetrics.getCronJobsMetrics();
        }
        try
        {
            Collection<String> cachedCronJobsRunning = getRunningOrRestartedCronJobs();
            this.cronJobMetrics.set(new CronJobMetrics(Instant.now(), cachedCronJobsRunning));
            this.eventService.publishEvent((AbstractEvent)new CronJobInfoEvent(cachedCronJobsRunning));
            return ((CronJobMetrics)this.cronJobMetrics.get()).getCronJobsMetrics();
        }
        catch(SystemException e)
        {
            LOG.warn(String.valueOf(e));
            return oldMetrics.getCronJobsMetrics();
        }
    }


    private boolean isCacheExpired(CronJobMetrics oldMetrics)
    {
        if(oldMetrics == null)
        {
            return true;
        }
        return (oldMetrics.getCacheExpiry().equals(Instant.EPOCH) || oldMetrics.getCacheExpiry()
                        .plus(this.duration)
                        .isBefore(Instant.now()));
    }


    private Collection<String> getRunningOrRestartedCronJobs() throws SystemException
    {
        Collection<CronJobModel> cronJobModelList = this.cronJobService.getRunningOrRestartedCronJobs();
        LOG.debug("Getting cronJobs list from DB");
        Collection<String> list = new ArrayList<>(cronJobModelList.size());
        for(CronJobModel cj : cronJobModelList)
        {
            list.add(cj.getCode());
        }
        return list;
    }
}
