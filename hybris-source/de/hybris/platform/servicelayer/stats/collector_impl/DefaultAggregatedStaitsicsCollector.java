package de.hybris.platform.servicelayer.stats.collector_impl;

import de.hybris.platform.servicelayer.stats.AbstractStatisticsCollector;
import de.hybris.platform.servicelayer.stats.AggregatedStatisticsCollector;
import de.hybris.platform.servicelayer.stats.BasicStatisticsCollector;
import java.util.ArrayList;
import java.util.List;

public class DefaultAggregatedStaitsicsCollector extends AbstractStatisticsCollector implements AggregatedStatisticsCollector
{
    private final List<BasicStatisticsCollector> collectors;


    public DefaultAggregatedStaitsicsCollector(String name, String label, String color)
    {
        super(name, label, color);
        this.collectors = new ArrayList<>();
    }


    public List<BasicStatisticsCollector> getContainedCollectors()
    {
        return this.collectors;
    }


    public boolean addContainedCollector(BasicStatisticsCollector collector)
    {
        return this.collectors.add(collector);
    }


    public boolean removeContainedCollector(BasicStatisticsCollector collector)
    {
        return this.collectors.remove(collector);
    }


    public boolean evaluateValue(float value)
    {
        return (value > 0.0F);
    }
}
