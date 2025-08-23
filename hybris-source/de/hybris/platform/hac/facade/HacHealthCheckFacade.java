package de.hybris.platform.hac.facade;

import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheckRegistry;
import java.text.MessageFormat;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;

public class HacHealthCheckFacade
{
    private HealthCheckRegistry healthCheckRegistry;
    private MetricRegistry metricRegistry;


    public Map<String, HealthCheck.Result> healthCheck()
    {
        Map<String, HealthCheck.Result> results = this.healthCheckRegistry.runHealthChecks();
        return results;
    }


    public boolean isAllHealthy(Map<String, HealthCheck.Result> results)
    {
        return results.values().stream().allMatch(HealthCheck.Result::isHealthy);
    }


    public void updateMetricForHealthyStatus()
    {
        updateMetric("healthy-check");
    }


    public void updateMetricForUnhealthyStatus()
    {
        updateMetric("unhealthy-check");
    }


    private void updateMetric(String name)
    {
        String counterName = MessageFormat.format("name={0}", new Object[] {MetricRegistry.name(name, new String[] {"count"})});
        Counter counter = this.metricRegistry.counter(counterName);
        counter.inc();
    }


    @Required
    public HacHealthCheckFacade setHealthCheckRegistry(HealthCheckRegistry healthCheckRegistry)
    {
        this.healthCheckRegistry = healthCheckRegistry;
        return this;
    }


    @Required
    public HacHealthCheckFacade setMetricRegistry(MetricRegistry metricRegistry)
    {
        this.metricRegistry = metricRegistry;
        return this;
    }
}
