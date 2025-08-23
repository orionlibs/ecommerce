package de.hybris.platform.metrics.dropwizard;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Counting;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.Meter;
import com.codahale.metrics.Metered;
import com.codahale.metrics.MetricFilter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.ScheduledReporter;
import com.codahale.metrics.Snapshot;
import com.codahale.metrics.Timer;
import com.google.common.base.Splitter;
import java.util.Map;
import java.util.Objects;
import java.util.SortedMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import mjson.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class Slf4jMdcMetricsReporter extends ScheduledReporter
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Slf4jMdcMetricsReporter.class);


    Slf4jMdcMetricsReporter(MetricRegistry registry, String name, MetricFilter filter, TimeUnit rateUnit, TimeUnit durationUnit, ScheduledExecutorService executor, boolean shutdownExecutorOnStop)
    {
        super(registry, name, filter, rateUnit, durationUnit, executor, shutdownExecutorOnStop);
    }


    protected void logTimer(String name, Timer timer)
    {
        if(isLogLevelDisabled())
        {
            return;
        }
        Json metrics = prepareJson(name, "TIMER");
        addCountMetricToMap((Counting)timer, metrics);
        Snapshot snapshot = timer.getSnapshot();
        addSnapshotMetricsToMap(snapshot, metrics);
        addMeteredMetricsToMap((Metered)timer, metrics);
        log(name, metrics);
    }


    protected void logGauge(String name, Gauge<?> gauge)
    {
        if(isLogLevelDisabled())
        {
            return;
        }
        Json metrics = prepareJson(name, "GAUGE");
        metrics.set("value", gauge.getValue());
        log(name, metrics);
    }


    protected void logCounter(String name, Counter counter)
    {
        if(isLogLevelDisabled())
        {
            return;
        }
        Json metrics = prepareJson(name, "COUNTER");
        addCountMetricToMap((Counting)counter, metrics);
        log(name, metrics);
    }


    protected void logMeter(String name, Meter meter)
    {
        if(isLogLevelDisabled())
        {
            return;
        }
        Json metrics = prepareJson(name, "METER");
        addMeteredMetricsToMap((Metered)meter, metrics);
        addCountMetricToMap((Counting)meter, metrics);
        log(name, metrics);
    }


    private void logHistogram(String name, Histogram histogram)
    {
        if(isLogLevelDisabled())
        {
            return;
        }
        Json metrics = prepareJson(name, "HISTOGRAM");
        Snapshot snapshot = histogram.getSnapshot();
        addCountMetricToMap((Counting)histogram, metrics);
        addSnapshotMetricsToMap(snapshot, metrics);
        log(name, metrics);
    }


    private Json prepareJson(String name, String type)
    {
        Map<String, String> parts = Splitter.on(",").withKeyValueSeparator("=").split(name);
        Json object = Json.object().set("type", type).set("name", name);
        if(!parts.isEmpty())
        {
            Json nameParts = Json.object();
            Objects.requireNonNull(nameParts);
            parts.forEach(nameParts::set);
            object.set("nameParts", nameParts);
        }
        return object;
    }


    private void log(String name, Json metrics)
    {
        MDC.MDCCloseable ignored = MDC.putCloseable("metrics", metrics.toString());
        try
        {
            LOGGER.debug("Logging metric: {}", name);
            if(ignored != null)
            {
                ignored.close();
            }
        }
        catch(Throwable throwable)
        {
            if(ignored != null)
            {
                try
                {
                    ignored.close();
                }
                catch(Throwable throwable1)
                {
                    throwable.addSuppressed(throwable1);
                }
            }
            throw throwable;
        }
    }


    private boolean isLogLevelDisabled()
    {
        return !LOGGER.isDebugEnabled();
    }


    private void addCountMetricToMap(Counting counting, Json metrics)
    {
        metrics.set("count", Long.valueOf(counting.getCount()));
    }


    private void addMeteredMetricsToMap(Metered metered, Json metrics)
    {
        metrics.set("mean_rate", Double.valueOf(convertRate(metered.getMeanRate())));
        metrics.set("m1", Double.valueOf(convertRate(metered.getOneMinuteRate())));
        metrics.set("m5", Double.valueOf(convertRate(metered.getFiveMinuteRate())));
        metrics.set("m15", Double.valueOf(convertRate(metered.getFifteenMinuteRate())));
        metrics.set("rate_unit", getRateUnit());
    }


    private void addSnapshotMetricsToMap(Snapshot snapshot, Json metrics)
    {
        metrics.set("min", Double.valueOf(convertDuration(snapshot.getMin())));
        metrics.set("max", Double.valueOf(convertDuration(snapshot.getMax())));
        metrics.set("mean", Double.valueOf(convertDuration(snapshot.getMean())));
        metrics.set("stdev", Double.valueOf(convertDuration(snapshot.getStdDev())));
        metrics.set("median", Double.valueOf(convertDuration(snapshot.getMedian())));
        metrics.set("p75", Double.valueOf(convertDuration(snapshot.get75thPercentile())));
        metrics.set("p95", Double.valueOf(convertDuration(snapshot.get95thPercentile())));
        metrics.set("p98", Double.valueOf(convertDuration(snapshot.get98thPercentile())));
        metrics.set("p99", Double.valueOf(convertDuration(snapshot.get99thPercentile())));
        metrics.set("p999", Double.valueOf(convertDuration(snapshot.get999thPercentile())));
        metrics.set("duration_unit", getDurationUnit());
    }


    protected String getRateUnit()
    {
        return "events/" + super.getRateUnit();
    }


    public void report(SortedMap<String, Gauge> gaugeMap, SortedMap<String, Counter> counterMap, SortedMap<String, Histogram> histogramMap, SortedMap<String, Meter> meterMap, SortedMap<String, Timer> timerMap)
    {
        for(Map.Entry<String, Timer> entry : timerMap.entrySet())
        {
            logTimer(entry.getKey(), entry.getValue());
        }
        for(Map.Entry<String, Gauge> entry : gaugeMap.entrySet())
        {
            logGauge(entry.getKey(), entry.getValue());
        }
        for(Map.Entry<String, Counter> entry : counterMap.entrySet())
        {
            logCounter(entry.getKey(), entry.getValue());
        }
        for(Map.Entry<String, Meter> entry : meterMap.entrySet())
        {
            logMeter(entry.getKey(), entry.getValue());
        }
        for(Map.Entry<String, Histogram> entry : histogramMap.entrySet())
        {
            logHistogram(entry.getKey(), entry.getValue());
        }
    }
}
