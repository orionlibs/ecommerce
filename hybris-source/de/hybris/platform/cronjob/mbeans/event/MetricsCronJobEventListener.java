package de.hybris.platform.cronjob.mbeans.event;

import de.hybris.platform.cronjob.mbeans.MetricsCronJobHolder;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;
import java.time.Instant;

public class MetricsCronJobEventListener extends AbstractEventListener<CronJobInfoEvent>
{
    private final MetricsCronJobHolder metricsCronJobHolder;


    public MetricsCronJobEventListener(MetricsCronJobHolder metricsCronJobHolder)
    {
        this.metricsCronJobHolder = metricsCronJobHolder;
    }


    protected void onEvent(CronJobInfoEvent event)
    {
        this.metricsCronJobHolder.cacheCronJobMetrics(Instant.ofEpochMilli(event.getTimestamp()), event.getCronJobs());
    }
}
