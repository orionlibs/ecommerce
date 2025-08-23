package de.hybris.platform.metrics.dropwizard;

import com.codahale.metrics.MetricFilter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.ScheduledReporter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

public class Slf4jMdcMetricsReporterFactory implements MetricsReporterFactory
{
    public ScheduledReporter createMetricReporter(MetricRegistry metricRegistry, String reporterName, MetricFilter metricFilter, ScheduledExecutorService executorService)
    {
        return (ScheduledReporter)new Slf4jMdcMetricsReporter(metricRegistry, reporterName, metricFilter, TimeUnit.SECONDS, TimeUnit.MILLISECONDS, executorService, false);
    }


    public MetricFilter getMetricFilter(Map<String, String> filters)
    {
        List<SpelExpressionMetricFilter> spelFilters = (List<SpelExpressionMetricFilter>)MapUtils.emptyIfNull(filters).values().stream().filter(StringUtils::isNotBlank).map(SpelExpressionMetricFilter::new).collect(Collectors.toList());
        if(spelFilters.size() == 1)
        {
            return (MetricFilter)spelFilters.get(0);
        }
        if(!spelFilters.isEmpty())
        {
            return (MetricFilter)new OrMetricsFilter(spelFilters);
        }
        return (MetricFilter)new SpelExpressionMetricFilter("true");
    }
}
