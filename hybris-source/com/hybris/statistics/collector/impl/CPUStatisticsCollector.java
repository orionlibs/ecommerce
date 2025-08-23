package com.hybris.statistics.collector.impl;

import com.hybris.statistics.collector.SystemStatisticsCollector;
import java.util.HashMap;
import java.util.Map;

public class CPUStatisticsCollector implements SystemStatisticsCollector<Map<String, Map<String, Integer>>>
{
    public Map<String, Map<String, Integer>> collectStatistics()
    {
        Map<String, Integer> availableCPU = new HashMap<String, Integer>();
        availableCPU.put("number of cores", Integer.valueOf(Runtime.getRuntime().availableProcessors()));
        Map<String, Map<String, Integer>> result = new HashMap<String, Map<String, Integer>>();
        result.put("CPU", availableCPU);
        return result;
    }
}
