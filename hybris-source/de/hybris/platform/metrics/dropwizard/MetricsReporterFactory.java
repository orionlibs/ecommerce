package de.hybris.platform.metrics.dropwizard;

import com.codahale.metrics.MetricFilter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.ScheduledReporter;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;

public interface MetricsReporterFactory
{
    ScheduledReporter createMetricReporter(MetricRegistry paramMetricRegistry, String paramString, MetricFilter paramMetricFilter, ScheduledExecutorService paramScheduledExecutorService);


    MetricFilter getMetricFilter(Map<String, String> paramMap);
}
