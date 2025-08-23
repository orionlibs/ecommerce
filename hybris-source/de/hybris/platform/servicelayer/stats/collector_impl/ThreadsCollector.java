package de.hybris.platform.servicelayer.stats.collector_impl;

import de.hybris.platform.servicelayer.stats.AbstractStatisticsCollector;
import de.hybris.platform.servicelayer.stats.BasicStatisticsCollector;
import de.hybris.platform.servicelayer.stats.MonitoringFacade;
import java.util.Map;
import javax.annotation.Resource;

public class ThreadsCollector extends AbstractStatisticsCollector implements BasicStatisticsCollector
{
    @Resource(name = "defaultStatMonitoringFacade")
    private MonitoringFacade monitoringFacade;


    public ThreadsCollector()
    {
        super("threads", "Threads", "#ABABAB");
    }


    public float collect()
    {
        try
        {
            Map threads = this.monitoringFacade.getThreadStat();
            Integer threadCount = (Integer)threads.get("threadCount");
            return threadCount.floatValue();
        }
        catch(Exception e)
        {
            setEnabled(false);
            return -1.0F;
        }
    }


    public boolean evaluateValue(float value)
    {
        return (value > 0.0F);
    }
}
