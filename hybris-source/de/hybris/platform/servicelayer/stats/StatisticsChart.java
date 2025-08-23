package de.hybris.platform.servicelayer.stats;

import java.util.List;

public interface StatisticsChart
{
    boolean addLine(StatisticsCollector paramStatisticsCollector);


    boolean removeLine(StatisticsCollector paramStatisticsCollector);


    StatisticsCollector getLine(String paramString);


    boolean containsLine(String paramString);


    List<StatisticsCollector> getAllLines();


    List<BasicStatisticsCollector> getBasicLines();


    List<AggregatedStatisticsCollector> getAggregatedLines();


    int getLineCount();


    String getName();


    List<StatisticsCollector> getViewLines(String paramString);


    List<AggregatedStatisticsCollector> getAllAggregatedCollectors();


    AggregatedStatisticsCollector getAggregatedCollector(String paramString);


    boolean addAggregatedCollector(AggregatedStatisticsCollector paramAggregatedStatisticsCollector);


    boolean addView(String paramString, List<StatisticsCollector> paramList);


    boolean addLinetoView(String paramString, StatisticsCollector paramStatisticsCollector);
}
