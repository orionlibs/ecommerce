package de.hybris.platform.servicelayer.event;

import java.util.Collection;

public class PublishEventContext
{
    private final int sourceNodeId;
    private final int targetNodeId;
    private final Collection<String> targetNodeGroups;


    private PublishEventContext(Builder builder)
    {
        this.sourceNodeId = builder.sourceNodeId;
        this.targetNodeId = builder.targetNodeId;
        this.targetNodeGroups = builder.targetNodeGroups;
    }


    public static Builder newBuilder()
    {
        return new Builder();
    }


    public int getSourceNodeId()
    {
        return this.sourceNodeId;
    }


    public int getTargetNodeId()
    {
        return this.targetNodeId;
    }


    public Collection<String> getTargetNodeGroups()
    {
        return this.targetNodeGroups;
    }
}
