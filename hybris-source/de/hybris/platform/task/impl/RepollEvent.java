package de.hybris.platform.task.impl;

import de.hybris.platform.servicelayer.event.ClusterAwareEvent;
import de.hybris.platform.servicelayer.event.TransactionAwareEvent;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;

public class RepollEvent extends AbstractEvent implements TransactionAwareEvent, ClusterAwareEvent
{
    private final Integer nodeId;
    private final String nodeGroupId;
    private final Object id;


    public RepollEvent(Integer nodeId, String nodeGroupId)
    {
        super((nodeId == null) ? Integer.valueOf(-1) : nodeId);
        this.nodeId = nodeId;
        this.nodeGroupId = nodeGroupId;
        this.id = (this.nodeId == null) ? getClass() : (getClass().getName() + "-node#" + getClass().getName()).intern();
    }


    public boolean publish(int sourceNodeId, int targetNodeId)
    {
        return (this.nodeId == null || this.nodeId.intValue() == targetNodeId);
    }


    public String getNodeGroupId()
    {
        return this.nodeGroupId;
    }


    public Integer getNodeId()
    {
        return this.nodeId;
    }


    public Object getId()
    {
        return this.id;
    }


    public boolean publishOnCommitOnly()
    {
        return true;
    }
}
