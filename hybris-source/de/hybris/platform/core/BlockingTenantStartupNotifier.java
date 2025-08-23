package de.hybris.platform.core;

import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableList;
import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BlockingTenantStartupNotifier implements TenantStartupNotifier
{
    private static final Logger LOG = LoggerFactory.getLogger(BlockingTenantStartupNotifier.class);
    private final Queue<TenantListener> listeners = new ConcurrentLinkedQueue<>();
    private final AtomicReference<CountDownLatch> notifyInProgress = new AtomicReference<>();
    private final ThreadLocal<Boolean> inStartupNotifyingTL = new ThreadLocal<>();
    private final Tenant tenant;


    public BlockingTenantStartupNotifier(Tenant tenant)
    {
        this.tenant = tenant;
    }


    public void scheduleNotifyTenantListenersAboutStartup(List<TenantListener> listeners)
    {
        ImmutableList<TenantListener> listenersToSchedule = ImmutableList.copyOf(listeners);
        LOG.info("Scheduling {} tenant startup listeners to be notified for tenant {}",
                        Integer.valueOf(listenersToSchedule.size()), this.tenant);
        this.listeners.clear();
        this.listeners.addAll((Collection<? extends TenantListener>)listenersToSchedule);
    }


    public void executeStartupNotify()
    {
        if(nothingToDo() || isReentrantNotifyingThread())
        {
            return;
        }
        CountDownLatch myLock = new CountDownLatch(1);
        CountDownLatch currentLock = this.notifyInProgress.accumulateAndGet(myLock, (previousLock, newLock) -> (previousLock == null) ? newLock : previousLock);
        if(currentLock != myLock)
        {
            LOG.info("Other thread is already notifying listeners for tenant {}.", this.tenant);
            waitForOtherThreadToFinishNotifications(currentLock);
            return;
        }
        LOG.info("Starting notifing listeners for tenant {}.", this.tenant);
        startThreadNotifyingStartup();
        try
        {
            notifyAllListeners();
        }
        finally
        {
            LOG.info("Finished notifing listeners for tenant {}.", this.tenant);
            currentLock.countDown();
            this.notifyInProgress.set(null);
            finishedThreadNotifyingStartup();
        }
    }


    private void waitForOtherThreadToFinishNotifications(CountDownLatch currentLock)
    {
        try
        {
            LOG.info("Waiting for other thread to finish notifying listeners for tenant {}.", this.tenant);
            Stopwatch waitTime = Stopwatch.createStarted();
            currentLock.await();
            LOG.info("Finished waiting for tenant {} in {}.", this.tenant, waitTime);
        }
        catch(InterruptedException e)
        {
            LOG.info("Waiting has been interrupted.");
            Thread.currentThread().interrupt();
        }
    }


    private void notifyAllListeners()
    {
        LOG.info("Executing startup listeners for tenant {}.", this.tenant);
        ClassLoaderUtils.executeWithWebClassLoaderParentIfNeeded(() -> {
            Stopwatch allListenersTime = Stopwatch.createStarted();
            while(!this.listeners.isEmpty())
            {
                TenantListener listener = this.listeners.remove();
                LOG.info("Notifying {} for tenant {}.", listener, this.tenant);
                if(listener == null)
                {
                    continue;
                }
                Stopwatch listenerTime = Stopwatch.createStarted();
                try
                {
                    listener.afterTenantStartUp(this.tenant);
                    LOG.info("{} has finished successfully in {}.", listener, listenerTime);
                }
                catch(Exception e)
                {
                    LOG.error("Failed to notify {} about tenant {} startup. Failed after {}.", new Object[] {listener, this.tenant, listenerTime, e});
                }
            }
            LOG.info("All listeners for tenant {} have been notified in {}.", this.tenant, allListenersTime);
        });
    }


    private boolean nothingToDo()
    {
        if(!this.listeners.isEmpty())
        {
            return false;
        }
        CountDownLatch inProgress = this.notifyInProgress.get();
        return (inProgress == null || inProgress.getCount() == 0L);
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
}
