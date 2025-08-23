package de.hybris.platform.hac.facade;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.HashMap;
import java.util.Map;

public class HacMemoryFacade
{
    private static final long MEGA = 1048576L;


    public Map<String, Long> memoryData()
    {
        return getRuntimeInfoMap();
    }


    public Map<String, Long> memoryGC()
    {
        System.gc();
        return getRuntimeInfoMap();
    }


    private Map<String, Long> getRuntimeInfoMap()
    {
        Runtime runtime = Runtime.getRuntime();
        long freeMemory = runtime.freeMemory() / 1048576L;
        long totalMemory = runtime.totalMemory() / 1048576L;
        long usedMemory = totalMemory - freeMemory;
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heap = memoryBean.getHeapMemoryUsage();
        MemoryUsage nonheap = memoryBean.getNonHeapMemoryUsage();
        Map<Object, Object> res = new HashMap<>();
        res.put("freeMemory", Long.valueOf(freeMemory));
        res.put("totalMemory", Long.valueOf(totalMemory));
        res.put("usedMemory", Long.valueOf(usedMemory));
        res.put("heap_init", Long.valueOf(heap.getInit() / 1048576L));
        res.put("heap_used", Long.valueOf(heap.getUsed() / 1048576L));
        res.put("heap_comitted", Long.valueOf(heap.getCommitted() / 1048576L));
        res.put("heap_max", Long.valueOf(heap.getMax() / 1048576L));
        res.put("nonheap_init", Long.valueOf(nonheap.getInit() / 1048576L));
        res.put("nonheap_used", Long.valueOf(nonheap.getUsed() / 1048576L));
        res.put("nonheap_comitted", Long.valueOf(nonheap.getCommitted() / 1048576L));
        res.put("nonheap_max", Long.valueOf(nonheap.getMax() / 1048576L));
        return (Map)res;
    }
}
