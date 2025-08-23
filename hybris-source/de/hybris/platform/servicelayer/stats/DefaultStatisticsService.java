package de.hybris.platform.servicelayer.stats;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.suspend.SystemIsSuspendedException;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

public class DefaultStatisticsService implements StatisticsService, InitializingBean, DisposableBean
{
    private StatisticsData dataHolder;
    private final ConcurrentMap<String, StatisticsChart> chartsToCollect = new ConcurrentHashMap<>();
    private Collection<StatisticsChart> configuredCharts;
    private final ConcurrentMap<String, BasicStatisticsCollector> collectorsToCollect = new ConcurrentHashMap<>();
    private Collection<BasicStatisticsCollector> configuredCollectors;
    private long interval;
    private StatDaemonThread thread;
    private static final Logger LOG = Logger.getLogger(StatisticsService.class);
    private boolean standaloneEnabled = false;


    public void setInterval(long interval)
    {
        this.interval = interval;
        if(this.thread != null)
        {
            this.thread.setInterval(interval);
        }
    }


    public void setDataHolder(StatisticsData dataHolder)
    {
        this.dataHolder = dataHolder;
    }


    public synchronized boolean addCollector(BasicStatisticsCollector collector)
    {
        LOG.info("adding collector " + collector.getName());
        try
        {
            if(this.collectorsToCollect.putIfAbsent(collector.getName(), collector) == null)
            {
                this.dataHolder.addDataCollector(collector.getName());
                return true;
            }
        }
        catch(Exception e)
        {
            LOG.error("error adding statistics collector", e);
        }
        return false;
    }


    public synchronized boolean removeCollector(String name)
    {
        boolean successfull = (this.collectorsToCollect.remove(name) != null);
        if(this.dataHolder.containsDataCollector(name))
        {
            successfull &= this.dataHolder.removeDataCollector(name);
        }
        return successfull;
    }


    public synchronized boolean addChart(StatisticsChart chart)
    {
        LOG.info("adding chart " + chart.getName());
        boolean result = true;
        this.chartsToCollect.put(chart.getName(), chart);
        for(StatisticsCollector collector : chart.getAllLines())
        {
            result &= this.dataHolder.addDataCollector(collector.getName());
        }
        return result;
    }


    public synchronized boolean removeChart(String name)
    {
        try
        {
            StatisticsChart chart = this.chartsToCollect.get(name);
            if(chart != null)
            {
                for(StatisticsCollector collector : chart.getAllLines())
                {
                    this.dataHolder.removeDataCollector(collector.getName());
                }
                this.chartsToCollect.remove(name);
            }
            return true;
        }
        catch(Exception e)
        {
            LOG.error("Removal of statistics-chart [" + name + "] failed.");
            return false;
        }
    }


    public void destroy()
    {
        stopThread();
    }


    public void afterPropertiesSet()
    {
        this.thread = new StatDaemonThread(this, this.interval);
        if(CollectionUtils.isNotEmpty(this.configuredCharts))
        {
            for(StatisticsChart chart : this.configuredCharts)
            {
                if(!this.dataHolder.containsDataCollector(chart.getName()))
                {
                    addChart(chart);
                }
            }
        }
        if(CollectionUtils.isNotEmpty(this.configuredCollectors))
        {
            for(BasicStatisticsCollector coll : this.configuredCollectors)
            {
                if(!this.dataHolder.containsDataCollector(coll.getName()))
                {
                    addCollector(coll);
                }
            }
        }
        if(this.standaloneEnabled || !Registry.isStandaloneMode())
        {
            startThread();
        }
        else
        {
            LOG.info("Collecting StatisticsData disabled when not in Standalone Mode.");
        }
    }


    private void startThread()
    {
        try
        {
            this.thread.start();
        }
        catch(IllegalThreadStateException e)
        {
            LOG.error("StatDaemonThread could not be started. Thread is already running." + e.getLocalizedMessage());
        }
    }


    private void stopThread()
    {
        this.thread.abort();
    }


    private void collectAggregated(AggregatedStatisticsCollector collector, long timestamp)
    {
        if(!this.dataHolder.containsDataCollector(collector.getName()))
        {
            this.dataHolder.addDataCollector(collector.getName());
        }
        float value = 0.0F;
        for(BasicStatisticsCollector basic : collector.getContainedCollectors())
        {
            float curr = this.dataHolder.getCollectorValue(basic.getName(), timestamp);
            value += (curr > 0.0F) ? curr : 0.0F;
        }
        if(collector.evaluateValue(value))
        {
            this.dataHolder.putData(collector.getName(), timestamp, value);
        }
    }


    private void collectData(BasicStatisticsCollector collector, long timestamp)
    {
        if(!this.dataHolder.containsDataCollector(collector.getName()))
        {
            this.dataHolder.addDataCollector(collector.getName());
        }
        float val = -1.0F;
        try
        {
            if(!collector.getEnabled())
            {
                return;
            }
            val = collector.collect();
            if(collector.evaluateValue(val))
            {
                this.dataHolder.putData(collector.getName(), timestamp, val);
            }
            else
            {
                this.dataHolder.balanceDataLevels(collector.getName());
            }
        }
        catch(SystemIsSuspendedException e)
        {
            LOG.info("System is SUSPENDED. Data-collector [" + collector.getName() + "] is paused");
        }
        catch(Exception e)
        {
            LOG.error("Failed collecting and putting data from data-collector [" + collector.getName() + "]");
        }
    }


    public StatisticsChart getChart(String name)
    {
        return this.chartsToCollect.containsKey(name) ? this.chartsToCollect.get(name) : null;
    }


    public void setCharts(Collection<StatisticsChart> charts)
    {
        this.configuredCharts = charts;
    }


    public void setCollectors(Collection<BasicStatisticsCollector> collectors)
    {
        this.configuredCollectors = collectors;
    }


    public void setStandaloneEnabled(boolean standaloneEnabled)
    {
        this.standaloneEnabled = standaloneEnabled;
    }
}
