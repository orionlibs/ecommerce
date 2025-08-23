package de.hybris.platform.regioncache.generation.impl;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.google.common.util.concurrent.AtomicLongMap;
import de.hybris.platform.metrics.dropwizard.MetricUtils;
import de.hybris.platform.regioncache.generation.GenerationalCounterService;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TypeCodeGenerationalCounterService implements GenerationalCounterService<String>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(TypeCodeGenerationalCounterService.class);
    private static final int DEBUG_INFO_GAP = 1000;
    private final ConcurrentHashMap<String, AtomicLongMap<String>> generations = new ConcurrentHashMap<>(4);
    private final AtomicLong printInfoCounter = new AtomicLong();
    private final MetricUtils.CachedMetrics<String> metrics;
    private boolean debugMode;


    public TypeCodeGenerationalCounterService()
    {
        this(null);
    }


    public TypeCodeGenerationalCounterService(MetricRegistry metricRegistry)
    {
        this.metrics = MetricUtils.metricCache(metricRegistry);
    }


    public long[] getGenerations(String[] types, String tenant)
    {
        long[] result = new long[types.length];
        AtomicLongMap<String> counters = getTenantCounters(tenant);
        for(int i = 0; i < types.length; i++)
        {
            result[i] = counters.get(types[i]);
        }
        return result;
    }


    public Map<String, Long> getGenerations(String tenant)
    {
        return getTenantCounters(tenant).asMap();
    }


    private Optional<Meter> getMeter(String type, String tenant)
    {
        return this.metrics.getMeter(type, tenant, ctx -> MetricUtils.metricName("cache", new String[] {"generation", "increments"}).extension("core").module("cache").tenant(tenant).tag("code", (String)ctx.getMetricKey()).build());
    }


    public void incrementGeneration(String type, String tenant)
    {
        try
        {
            getTenantCounters(tenant).incrementAndGet(type);
            getMeter(type, tenant).ifPresent(Meter::mark);
        }
        finally
        {
            printStatistics();
        }
    }


    private AtomicLongMap<String> getTenantCounters(String tenant)
    {
        return this.generations.computeIfAbsent(tenant, t -> AtomicLongMap.create());
    }


    public void clear()
    {
        this.generations.clear();
    }


    public void setDebugMode(boolean debugMode)
    {
        this.debugMode = debugMode;
    }


    private void printStatistics()
    {
        if(this.debugMode && LOGGER.isDebugEnabled() && this.printInfoCounter.getAndIncrement() % 1000L == 0L)
        {
            LOGGER.debug(this.generations.toString());
        }
    }
}
