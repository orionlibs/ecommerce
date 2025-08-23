package de.hybris.platform.servicelayer.stats;

import java.util.Map;

public interface MonitoringFacade
{
    Map<String, String> getSystemUptime();


    Map<String, Object> getMemoryStat();


    Map<String, Object> getThreadStat();


    Map<String, Object> getOsStat();
}
