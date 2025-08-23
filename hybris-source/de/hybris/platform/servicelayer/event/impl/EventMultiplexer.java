package de.hybris.platform.servicelayer.event.impl;

import de.hybris.platform.servicelayer.cluster.ClusterService;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

@Deprecated(since = "ages", forRemoval = true)
public class EventMultiplexer implements ApplicationListener
{
    private List<AbstractEventListener> listeners = new ArrayList<>();
    private ClusterService clusterService;


    public void onApplicationEvent(ApplicationEvent event)
    {
        if(event instanceof AbstractEvent && EventUtils.matchCluster((AbstractEvent)event, this.clusterService.getClusterIslandId(), this.clusterService
                        .getClusterId(), this.clusterService
                        .getClusterGroups()))
        {
            for(AbstractEventListener listener : this.listeners)
            {
                listener.onApplicationEvent((AbstractEvent)event);
            }
        }
    }


    public synchronized boolean registerListener(AbstractEventListener listener)
    {
        boolean result;
        if(this.listeners.contains(listener))
        {
            result = false;
        }
        else
        {
            List<AbstractEventListener> newListeners = new ArrayList<>(this.listeners);
            newListeners.add(listener);
            this.listeners = newListeners;
            result = true;
        }
        return result;
    }


    public synchronized boolean unregisterListener(AbstractEventListener listener)
    {
        List<AbstractEventListener> newListeners = new ArrayList<>(this.listeners);
        boolean removed = newListeners.remove(listener);
        this.listeners = newListeners;
        return removed;
    }


    public List<AbstractEventListener> getListeners()
    {
        return Collections.unmodifiableList(this.listeners);
    }


    @Required
    public void setClusterService(ClusterService clusterService)
    {
        this.clusterService = clusterService;
    }
}
