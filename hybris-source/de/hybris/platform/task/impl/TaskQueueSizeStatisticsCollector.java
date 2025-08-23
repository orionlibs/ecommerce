package de.hybris.platform.task.impl;

import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.stats.AbstractStatisticsCollector;
import de.hybris.platform.servicelayer.stats.BasicStatisticsCollector;
import de.hybris.platform.util.MessageFormatUtils;
import org.springframework.beans.factory.annotation.Required;

public class TaskQueueSizeStatisticsCollector extends AbstractStatisticsCollector implements BasicStatisticsCollector
{
    private MetricRegistry metricRegistry;
    private final String tenantId;


    public TaskQueueSizeStatisticsCollector()
    {
        super("taskQueueSize_" + Registry.getCurrentTenantNoFallback().getTenantID(), "Task Queue Size", "#000000");
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
        float s = (float)getCurrentQueueSize();
        return s;
    }


    protected long getCurrentQueueSize()
    {
        try
        {
            Counter c = this.metricRegistry.counter(metricName(DefaultTaskService.QUEUE_SIZE_METRIC));
            if(c != null)
            {
                return c.getCount();
            }
            return -1L;
        }
        catch(Exception e)
        {
            return -1L;
        }
    }


    public boolean evaluateValue(float value)
    {
        return (value >= 0.0F);
    }
}
