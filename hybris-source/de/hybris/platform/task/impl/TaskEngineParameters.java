package de.hybris.platform.task.impl;

import de.hybris.platform.core.Tenant;
import java.util.Collection;

public class TaskEngineParameters
{
    private final int maxThreads;
    private final int clusterNodeID;
    private final Collection<String> clusterGroupsForThisNode;
    private final Tenant tenant;
    private final boolean processTriggerTask;
    private final boolean exclusiveMode;
    private final int maxItemsToSchedule;


    protected TaskEngineParameters(ParametersBuilder builder)
    {
        this.maxThreads = builder.maxThreads;
        this.clusterNodeID = builder.clusterNodeID;
        this.clusterGroupsForThisNode = builder.clusterGroupsForThisNode;
        this.tenant = builder.tenant;
        this.processTriggerTask = builder.processTriggerTask;
        this.exclusiveMode = builder.exclusiveMode;
        this.maxItemsToSchedule = builder.maxItemsToSchedule;
    }


    public int getMaxThreads()
    {
        return this.maxThreads;
    }


    public int getMaxItemsToSchedule()
    {
        return this.maxItemsToSchedule;
    }


    public int getClusterNodeID()
    {
        return this.clusterNodeID;
    }


    public Collection<String> getClusterGroupsForThisNode()
    {
        return this.clusterGroupsForThisNode;
    }


    public Tenant getTenant()
    {
        return this.tenant;
    }


    public boolean isProcessTriggerTask()
    {
        return this.processTriggerTask;
    }


    public boolean isExclusiveMode()
    {
        return this.exclusiveMode;
    }
}
