package de.hybris.platform.metrics.dropwizard;

import com.codahale.metrics.MetricRegistry;
import de.hybris.platform.core.Registry;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;

public final class MetricUtils
{
    private static final Logger LOGGER = LoggerFactory.getLogger(MetricUtils.class);


    public static Optional<MetricRegistry> getMetricRegistry()
    {
        try
        {
            return Optional.of((MetricRegistry)Registry.getApplicationContext().getBean("metricRegistry", MetricRegistry.class));
        }
        catch(BeansException | IllegalStateException e)
        {
            LOGGER.info(e.getMessage());
            return Optional.empty();
        }
    }


    public static MetricNameBuilder metricName(String name, String... names)
    {
        return new MetricNameBuilder(name, names);
    }


    public static <K> CachedMetrics<K> metricCache()
    {
        return metricCache(null);
    }


    public static <K> CachedMetrics<K> metricCache(MetricRegistry metricRegistry)
    {
        return new CachedMetrics(metricRegistry);
    }
}
