package com.hybris.statistics.collector.impl;

import com.hybris.statistics.collector.SystemStatisticsCollector;
import java.util.HashMap;
import java.util.Map;

public class JdkStatisticsCollector implements SystemStatisticsCollector<Map<String, Map<String, String>>>
{
    public Map<String, Map<String, String>> collectStatistics()
    {
        Map<String, String> jdkStats = new HashMap<String, String>();
        jdkStats.put("java.vm.vendor", System.getProperty("java.vm.vendor"));
        jdkStats.put("java.vm.version", System.getProperty("java.vm.version"));
        jdkStats.put("java.version", System.getProperty("java.version"));
        Map<String, Map<String, String>> result = new HashMap<String, Map<String, String>>();
        result.put("JDK", jdkStats);
        return result;
    }
}
