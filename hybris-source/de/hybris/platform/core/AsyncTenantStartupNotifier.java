package de.hybris.platform.core;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AsyncTenantStartupNotifier implements TenantStartupNotifier
{
    private static final Logger LOG = LoggerFactory.getLogger(AsyncTenantStartupNotifier.class);
    private final Tenant tenant;
    private final ThreadLocal<Boolean> inStartupNotifyingTL = new ThreadLocal<>();
    private final Queue<TenantListener> startupNotifyQueue = new ConcurrentLinkedQueue<>();


    public AsyncTenantStartupNotifier(Tenant tenant)
    {
        this.tenant = tenant;
    }


    public void scheduleNotifyTenantListenersAboutStartup(List<TenantListener> listeners)
    {
        ImmutableList<TenantListener> listenersToSchedule = ImmutableList.copyOf(listeners);
        LOG.info("Scheduling {} tenant startup listeners to be notified for tenant {}",
                        Integer.valueOf(listenersToSchedule.size()), this.tenant);
        this.startupNotifyQueue.clear();
        this.startupNotifyQueue.addAll((Collection<? extends TenantListener>)listenersToSchedule);
    }


    public void executeStartupNotify()
    {
        if(mustNotifyStartupListeners() && !isReentrantNotifyingThread())
        {
            try
            {
                startThreadNotifyingStartup();
                executeStartupNotifyIfNecessary();
            }
            finally
            {
                finishedThreadNotifyingStartup();
            }
        }
    }


    private boolean mustNotifyStartupListeners()
    {
        return !this.startupNotifyQueue.isEmpty();
    }


    private boolean isReentrantNotifyingThread()
    {
        return Boolean.TRUE.equals(this.inStartupNotifyingTL.get());
    }


    private void startThreadNotifyingStartup()
    {
        this.inStartupNotifyingTL.set(Boolean.TRUE);
    }


    private void finishedThreadNotifyingStartup()
    {
        this.inStartupNotifyingTL.remove();
    }


    private void executeStartupNotifyIfNecessary()
    {
        LOG.info("Executing startup listeners for tenant {}.", this.tenant);
        TenantListener listener = this.startupNotifyQueue.poll();
        if(listener != null)
        {
            LOG.info("Notifying {} for tenant {}", listener, this.tenant);
            do
            {
                try
                {
                    listener.afterTenantStartUp(this.tenant);
                }
                catch(Exception e)
                {
                    LOG.error("Failed to notify {} about tenant {} startup.", new Object[] {listener, this.tenant, e});
                }
                listener = this.startupNotifyQueue.poll();
            }
            while(listener != null);
            LOG.info("No more listeners to notify for tenant {}.", this.tenant);
        }
        Preconditions.checkArgument(this.startupNotifyQueue.isEmpty(), "Startup listener queue is not empty");
    }
}
