package de.hybris.platform.servicelayer.stats.chart_impl;

import de.hybris.platform.servicelayer.stats.AggregatedStatisticsCollector;
import de.hybris.platform.servicelayer.stats.BasicStatisticsCollector;
import de.hybris.platform.servicelayer.stats.StatisticsChart;
import de.hybris.platform.servicelayer.stats.StatisticsCollector;
import de.hybris.platform.servicelayer.stats.StatisticsService;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;

public class DefaultStatisticsChart implements StatisticsChart
{
    private final String name;
    private final String unit;
    private List<BasicStatisticsCollector> basicLines;
    private List<AggregatedStatisticsCollector> aggregatedLines;
    private Map<String, List<StatisticsCollector>> views;


    public DefaultStatisticsChart(String name, String unit)
    {
        this(name, unit, null);
    }


    public DefaultStatisticsChart(String name, String unit, StatisticsService statisticsService)
    {
        this.name = name;
        this.unit = unit;
        this.basicLines = new ArrayList<>();
        this.views = new LinkedHashMap<>();
        this.aggregatedLines = new ArrayList<>();
        if(statisticsService != null)
        {
            statisticsService.addChart(this);
        }
    }


    public boolean addLine(StatisticsCollector collector)
    {
        if(collector instanceof BasicStatisticsCollector)
        {
            return this.basicLines.add((BasicStatisticsCollector)collector);
        }
        if(collector instanceof AggregatedStatisticsCollector)
        {
            return this.aggregatedLines.add((AggregatedStatisticsCollector)collector);
        }
        return false;
    }


    public boolean removeLine(StatisticsCollector collector)
    {
        if(collector instanceof BasicStatisticsCollector)
        {
            return this.basicLines.remove(collector);
        }
        if(collector instanceof AggregatedStatisticsCollector)
        {
            return this.aggregatedLines.remove(collector);
        }
        return false;
    }


    public StatisticsCollector getLine(String name)
    {
        for(BasicStatisticsCollector collector : this.basicLines)
        {
            if(StringUtils.equals(collector.getName(), name))
            {
                return (StatisticsCollector)collector;
            }
        }
        for(AggregatedStatisticsCollector collector : this.aggregatedLines)
        {
            if(StringUtils.equals(collector.getName(), name))
            {
                return (StatisticsCollector)collector;
            }
        }
        return null;
    }


    public List<StatisticsCollector> getAllLines()
    {
        List<StatisticsCollector> result = new ArrayList<>();
        result.addAll(this.basicLines);
        result.addAll(this.aggregatedLines);
        return result;
    }


    public int getLineCount()
    {
        return this.basicLines.size() + this.aggregatedLines.size();
    }


    public String getName()
    {
        return this.name;
    }


    public String getUnit()
    {
        return this.unit;
    }


    public boolean addView(String name, List<StatisticsCollector> collectors)
    {
        if(this.views != null && !this.views.containsKey(name))
        {
            this.views.put(name, collectors);
            return true;
        }
        return false;
    }


    public boolean addLinetoView(String name, StatisticsCollector collector)
    {
        if(this.views == null || !this.views.containsKey(name) || ((List)this.views.get(name)).contains(collector))
        {
            return false;
        }
        return ((List<StatisticsCollector>)this.views.get(name)).add(collector);
    }


    public List<StatisticsCollector> getViewLines(String view)
    {
        return this.views.containsKey(view) ? this.views.get(view) : getAllLines();
    }


    public void setBasicLines(List<BasicStatisticsCollector> lines)
    {
        this.basicLines = lines;
    }


    public void setAggregatedLines(List<AggregatedStatisticsCollector> lines)
    {
        this.aggregatedLines = lines;
    }


    public boolean containsLine(String name)
    {
        for(BasicStatisticsCollector collector : this.basicLines)
        {
            if(StringUtils.equals(collector.getName(), name))
            {
                return true;
            }
        }
        for(AggregatedStatisticsCollector collector : this.aggregatedLines)
        {
            if(StringUtils.equals(collector.getName(), name))
            {
                return true;
            }
        }
        return false;
    }


    public void setViews(Map<String, List<StatisticsCollector>> mapping)
    {
        this.views = mapping;
    }


    public AggregatedStatisticsCollector getAggregatedCollector(String aggregatedName)
    {
        for(AggregatedStatisticsCollector collector : this.aggregatedLines)
        {
            if(StringUtils.equals(collector.getName(), aggregatedName))
            {
                return collector;
            }
        }
        return null;
    }


    public boolean addAggregatedCollector(AggregatedStatisticsCollector collector)
    {
        if(containsLine(collector.getName()))
        {
            return false;
        }
        return this.aggregatedLines.add(collector);
    }


    public List<AggregatedStatisticsCollector> getAllAggregatedCollectors()
    {
        return this.aggregatedLines;
    }


    public List<BasicStatisticsCollector> getBasicLines()
    {
        return this.basicLines;
    }


    public List<AggregatedStatisticsCollector> getAggregatedLines()
    {
        return this.aggregatedLines;
    }
}
