package com.hybris.statistics.collector.impl;

import com.hybris.statistics.collector.SystemStatisticsCollector;
import java.util.HashMap;
import java.util.Map;

public class MemoryStatisticsCollector implements SystemStatisticsCollector<Map<String, Map<String, Long>>>
{
    public Map<String, Map<String, Long>> collectStatistics()
    {
        Map<String, Long> memStats = new HashMap<String, Long>();
        Runtime runtime = Runtime.getRuntime();
        memStats.put("maxMemory", Long.valueOf(runtime.maxMemory()));
        memStats.put("freeMemory", Long.valueOf(runtime.freeMemory()));
        memStats.put("totalMemory", Long.valueOf(runtime.totalMemory()));
        Map<String, Map<String, Long>> result = new HashMap<String, Map<String, Long>>();
        result.put("Memory", memStats);
        return result;
    }
}
