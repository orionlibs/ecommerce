package de.hybris.platform.jmx.mbeans.impl;

import de.hybris.platform.jmx.mbeans.ProfilingReportBean;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.log4j.Logger;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;

@ManagedResource
public class ProfilerReportPOJO implements ProfilingReportBean
{
    private static final Logger LOG = Logger.getLogger(ProfilerReportPOJO.class.getName());
    private final AtomicLong totalCount = new AtomicLong(0L);
    private final AtomicLong totalExecutionTime = new AtomicLong(0L);
    private AtomicLong maxExecutionTime = new AtomicLong(0L);
    private AtomicLong minExecutionTime = new AtomicLong(0L);
    private final AtomicLong failedExecutions = new AtomicLong(0L);


    @ManagedOperation
    public void logExecutionTime(long executionTime, long calledAt)
    {
        this.totalCount.getAndIncrement();
        this.totalExecutionTime.addAndGet(executionTime);
        if(executionTime > this.maxExecutionTime.get())
        {
            this.maxExecutionTime = new AtomicLong(executionTime);
        }
        if(this.minExecutionTime.get() > executionTime)
        {
            this.minExecutionTime = new AtomicLong(executionTime);
        }
    }


    @ManagedOperation
    public void logException()
    {
        this.failedExecutions.getAndIncrement();
    }


    @ManagedAttribute
    public long getMaxExecutionTime()
    {
        return this.maxExecutionTime.get();
    }


    @ManagedAttribute
    public long getFailedExecutions()
    {
        return this.failedExecutions.get();
    }


    @ManagedAttribute
    public long getMinExecutionTime()
    {
        return this.minExecutionTime.get();
    }


    @ManagedAttribute
    public long getTotalCount()
    {
        return this.totalCount.get();
    }


    @ManagedAttribute
    public long getTotalTime()
    {
        return this.totalExecutionTime.get();
    }
}
