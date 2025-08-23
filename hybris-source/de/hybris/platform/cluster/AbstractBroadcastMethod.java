package de.hybris.platform.cluster;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.log4j.Logger;

public abstract class AbstractBroadcastMethod implements BroadcastMethod
{
    private static final Logger LOG = Logger.getLogger(AbstractBroadcastMethod.class);
    private final List<BroadcastMessageListener> listeners = new CopyOnWriteArrayList<>();


    protected List<BroadcastMessageListener> getListeners()
    {
        return this.listeners;
    }


    protected void notifyMessgageReceived(RawMessage message)
    {
        for(BroadcastMessageListener listener : this.listeners)
        {
            try
            {
                if(listener.processMessage(message))
                {
                    break;
                }
            }
            catch(Exception e)
            {
                LOG.error("unexpected error notifying broadcast listener " + listener + " (error:" + e.getMessage() + ")", e);
            }
        }
    }


    public void registerProcessor(BroadcastMessageListener listener)
    {
        if(listener == null)
        {
            throw new NullPointerException("listener was null");
        }
        if(!this.listeners.contains(listener))
        {
            this.listeners.add(listener);
        }
    }


    public void unregisterProcessor(BroadcastMessageListener listener)
    {
        if(listener == null)
        {
            throw new NullPointerException("listener was null");
        }
        this.listeners.remove(listener);
    }


    public void init(BroadcastService service)
    {
    }


    public void shutdown()
    {
    }


    public Map<String, String> getSettings()
    {
        return Collections.EMPTY_MAP;
    }
}
