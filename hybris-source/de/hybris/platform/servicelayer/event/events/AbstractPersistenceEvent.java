package de.hybris.platform.servicelayer.event.events;

import de.hybris.platform.servicelayer.event.ClusterAwareEvent;
import de.hybris.platform.servicelayer.event.TransactionAwareEvent;
import java.io.Serializable;

public abstract class AbstractPersistenceEvent extends AbstractEvent implements TransactionAwareEvent, ClusterAwareEvent
{
    public AbstractPersistenceEvent()
    {
        throw new IllegalArgumentException();
    }


    public AbstractPersistenceEvent(Serializable pk)
    {
        super(pk);
    }


    public Object getId()
    {
        return getSource();
    }


    public boolean publishOnCommitOnly()
    {
        return true;
    }


    public boolean publish(int sourceNodeId, int targetNodeId)
    {
        return true;
    }
}
