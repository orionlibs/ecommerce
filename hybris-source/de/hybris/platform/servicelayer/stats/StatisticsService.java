package de.hybris.platform.servicelayer.stats;

public interface StatisticsService
{
    boolean addCollector(BasicStatisticsCollector paramBasicStatisticsCollector);


    boolean removeCollector(String paramString);


    boolean addChart(StatisticsChart paramStatisticsChart);


    boolean removeChart(String paramString);


    StatisticsChart getChart(String paramString);
}
