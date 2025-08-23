package de.hybris.platform.servicelayer.stats.collector_impl;

import de.hybris.platform.servicelayer.stats.AbstractStatisticsCollector;
import de.hybris.platform.servicelayer.stats.BasicStatisticsCollector;
import de.hybris.platform.servicelayer.stats.MonitoringFacade;
import java.lang.management.MemoryUsage;
import java.util.Map;
import javax.annotation.Resource;

public class UsedMemoryCollector extends AbstractStatisticsCollector implements BasicStatisticsCollector
{
    @Resource(name = "defaultStatMonitoringFacade")
    private MonitoringFacade monitoringFacade;


    public UsedMemoryCollector()
    {
        super("usedMemory", "Used Memory", "#FFFFFF");
    }


    public float collect()
    {
        try
        {
            Map memory = this.monitoringFacade.getMemoryStat();
            MemoryUsage heap = (MemoryUsage)memory.get("heap");
            MemoryUsage nonHeap = (MemoryUsage)memory.get("nonHeap");
            Float heapUsed = Float.valueOf((float)heap.getUsed());
            Float nonHeapUsed = Float.valueOf((float)nonHeap.getUsed());
            return (heapUsed.floatValue() + nonHeapUsed.floatValue()) / 1024.0F / 1024.0F;
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
