package de.hybris.platform.servicelayer.stats.collector_impl;

import de.hybris.platform.servicelayer.stats.AbstractStatisticsCollector;
import de.hybris.platform.servicelayer.stats.BasicStatisticsCollector;
import de.hybris.platform.servicelayer.stats.MonitoringFacade;
import java.util.Map;
import javax.annotation.Resource;

public class CpuWorkloadCollector extends AbstractStatisticsCollector implements BasicStatisticsCollector
{
    @Resource(name = "defaultStatMonitoringFacade")
    private MonitoringFacade monitoringFacade;


    public CpuWorkloadCollector()
    {
        super("cpuWorkload", "CPU Workload", "#ABABAB");
    }


    public float collect()
    {
        try
        {
            Map workload = this.monitoringFacade.getOsStat();
            Double loadFactor = (Double)workload.get("systemLoadAverage");
            Integer cores = (Integer)workload.get("availableProcessors");
            float cpuWorkload = loadFactor.floatValue() * 100.0F / cores.floatValue();
            return cpuWorkload;
        }
        catch(Exception e)
        {
            setEnabled(false);
            return -1.0F;
        }
    }


    public boolean evaluateValue(float value)
    {
        return (value >= 0.0F);
    }
}
