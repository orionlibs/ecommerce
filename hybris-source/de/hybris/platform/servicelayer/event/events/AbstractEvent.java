package de.hybris.platform.servicelayer.event.events;

import de.hybris.platform.servicelayer.event.impl.EventScope;
import java.io.Serializable;
import org.springframework.context.ApplicationEvent;

public abstract class AbstractEvent extends ApplicationEvent
{
    private static final Serializable EMPTY_SOURCE = (Serializable)new Object();
    private final Serializable source;
    private volatile EventScope eventScope;
    private volatile transient boolean fromCluster;


    public AbstractEvent()
    {
        this(EMPTY_SOURCE);
    }


    public AbstractEvent(Serializable source)
    {
        super(source);
        if(source == null)
        {
            throw new IllegalArgumentException("source cannot be null");
        }
        this.source = source;
    }


    public Object getSource()
    {
        return (this.source == null) ? super.getSource() : this.source;
    }


    public EventScope getScope()
    {
        return this.eventScope;
    }


    public void setScope(EventScope scope)
    {
        this.eventScope = scope;
    }


    public String toString()
    {
        return "Event: " + getClass().getName() + " (Source: " + getSource() + ", Scope: " + getScope() + ")";
    }


    public boolean isFromCluster()
    {
        return this.fromCluster;
    }


    public void setFromCluster(boolean fromCluster)
    {
        this.fromCluster = fromCluster;
    }
}
