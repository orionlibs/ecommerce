package de.hybris.platform.task.impl;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;
import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.stats.AbstractStatisticsCollector;
import de.hybris.platform.servicelayer.stats.BasicStatisticsCollector;
import de.hybris.platform.util.MessageFormatUtils;
import java.util.SortedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class TaskQueuePoolingQueueSizeStatisticsCollector extends AbstractStatisticsCollector implements BasicStatisticsCollector
{
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskQueuePoolingQueueSizeStatisticsCollector.class);
    private MetricRegistry metricRegistry;
    private final String tenantId;


    public TaskQueuePoolingQueueSizeStatisticsCollector()
    {
        super("taskQueuePoolingSize_" + Registry.getCurrentTenantNoFallback().getTenantID(), "Task Queue Pooling Size", "#0000FF");
        this.tenantId = Registry.getCurrentTenantNoFallback().getTenantID();
    }


    @Required
    public void setMetricRegistry(MetricRegistry registry)
    {
        this.metricRegistry = registry;
    }


    private String metricName(String name)
    {
        return MessageFormatUtils.format("tenant={0},extension=processing,module=taskEngine,name={1}", new Object[] {this.tenantId, name});
    }


    public float collect()
    {
        float s = getCurrentPoolingQueueSize();
        return s;
    }


    protected DefaultTaskService.PoolingQueueSizeGauge getPoolingQueueSizeGauge()
    {
        String metricName = metricName(DefaultTaskService.POOLING_QUEUE_SIZE_METRIC);
        SortedMap<String, Gauge> gauges = this.metricRegistry.getGauges((name, metric) -> (name.contains(metricName) && metric instanceof DefaultTaskService.PoolingQueueSizeGauge));
        return gauges.isEmpty() ? null : gauges.values().iterator().next();
    }


    protected int getCurrentPoolingQueueSize()
    {
        try
        {
            DefaultTaskService.PoolingQueueSizeGauge gauge = getPoolingQueueSizeGauge();
            if(gauge != null)
            {
                Integer v = gauge.getValue();
                return (v == null) ? -1 : v.intValue();
            }
            return -1;
        }
        catch(Exception e)
        {
            LOGGER.warn("error while retrieving current pooling queue size", e);
            return -1;
        }
    }


    public boolean evaluateValue(float value)
    {
        return (value >= 0.0F);
    }
}
