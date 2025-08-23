package de.hybris.platform.mediaconversion.os.process.impl;

import de.hybris.platform.mediaconversion.os.ProcessContext;
import de.hybris.platform.mediaconversion.os.process.ProcessContextRegistry;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class DefaultProcessContextRegistry implements ProcessContextRegistry
{
    private final AtomicInteger sequence = new AtomicInteger(38);
    private final Map<Integer, ProcessContext> contextRegistry = new ConcurrentHashMap<>();


    public int register(ProcessContext context)
    {
        int pid = this.sequence.getAndIncrement();
        this.contextRegistry.put(Integer.valueOf(pid), context);
        return pid;
    }


    public void unregister(int pid)
    {
        this.contextRegistry.remove(Integer.valueOf(pid));
    }


    public ProcessContext retrieve(int pid)
    {
        return this.contextRegistry.get(Integer.valueOf(pid));
    }
}
