package de.hybris.platform.jmx.mbeans;

public interface ProfilingReportBean extends ProfilingReportTemplate
{
    long getFailedExecutions();


    long getMaxExecutionTime();


    long getMinExecutionTime();


    long getTotalCount();


    long getTotalTime();
}
