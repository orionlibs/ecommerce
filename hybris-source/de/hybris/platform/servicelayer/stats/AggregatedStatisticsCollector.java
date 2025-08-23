package de.hybris.platform.servicelayer.stats;

import java.util.List;

public interface AggregatedStatisticsCollector extends StatisticsCollector
{
    List<BasicStatisticsCollector> getContainedCollectors();


    boolean addContainedCollector(BasicStatisticsCollector paramBasicStatisticsCollector);


    boolean removeContainedCollector(BasicStatisticsCollector paramBasicStatisticsCollector);


    boolean evaluateValue(float paramFloat);
}
