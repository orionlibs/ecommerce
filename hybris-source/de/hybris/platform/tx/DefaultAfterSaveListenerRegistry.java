package de.hybris.platform.tx;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.util.Config;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

public class DefaultAfterSaveListenerRegistry implements AfterSaveListenerRegistry, InitializingBean, DisposableBean
{
    private static final boolean DEFAULT_ASYNC_MODE = true;
    private static final int DEFAULT_SLEEP_TIME = 200;
    private static final int DEFAULT_BATCH_SIZE = 1024;
    private static final int DEFAULT_QUEUE_SIZE = 1024;
    private static final int DEFAULT_ACTIVE_TIME = (int)TimeUnit.SECONDS.toMillis(5L);
    public static final String CORE_AFTERSAVE_INTERVAL = "core.aftersave.interval";
    public static final String CORE_AFTERSAVE_BATCHSIZE = "core.aftersave.batchsize";
    public static final String CORE_AFTERSAVE_QUEUESIZE = "core.aftersave.queuesize";
    public static final String CORE_AFTERSAVE_ACTIVE_TIME = "core.aftersave.active.time";
    public static final String CORE_AFTERSAVE_ASYNC = "core.aftersave.async";
    private static final Logger LOG = Logger.getLogger(DefaultAfterSaveListenerRegistry.class);
    private final List<AfterSaveListener> listeners = new CopyOnWriteArrayList<>();
    private AfterSaveEventPublisherThread eventPublisherThread;
    private BlockingQueue<byte[]> queue;
    private boolean publishAsynchronously;


    public void setListeners(List<AfterSaveListener> listeners)
    {
        this.listeners.clear();
        if(CollectionUtils.isNotEmpty(listeners))
        {
            this.listeners.addAll(listeners);
        }
    }


    public void publishChanges(byte[][] encodedChanges)
    {
        if(ArrayUtils.isNotEmpty((Object[])encodedChanges))
        {
            if(isPublishAsynchronously() && isNotPublisherThread())
            {
                enqueueChangesForAsyncNotification(encodedChanges);
            }
            else
            {
                notifyListenersSynchronously(AfterSaveEventUtils.createEventsFromChanges(encodedChanges));
            }
        }
    }


    private boolean isNotPublisherThread()
    {
        return !(Thread.currentThread() instanceof AfterSaveEventPublisherThread);
    }


    protected void enqueueChangesForAsyncNotification(byte[][] changes)
    {
        for(byte[] change : changes)
        {
            try
            {
                this.queue.put(change);
            }
            catch(InterruptedException e)
            {
                Thread.currentThread().interrupt();
                LOG.warn("interrupted while enqueuing AfterSaveEvents - aborting: " + e.getMessage());
                break;
            }
        }
    }


    protected void notifyListenersSynchronously(Collection<AfterSaveEvent> events)
    {
        doNotifyListeners(events, this.listeners);
    }


    public void addListener(AfterSaveListener afterSaveListener)
    {
        this.listeners.add(afterSaveListener);
    }


    public void removeListener(AfterSaveListener afterSaveListener)
    {
        this.listeners.remove(afterSaveListener);
    }


    public void destroy() throws Exception
    {
        if(isPublishAsynchronously())
        {
            this.eventPublisherThread.interrupt();
        }
    }


    public void afterPropertiesSet() throws Exception
    {
        this.publishAsynchronously = isAsyncParam();
        if(this.publishAsynchronously)
        {
            initPublisherThread();
        }
    }


    protected synchronized void initPublisherThread()
    {
        if(this.eventPublisherThread != null)
        {
            throw new IllegalStateException("publisher thread already started");
        }
        Tenant tenant = Registry.getCurrentTenantNoFallback();
        this.queue = (BlockingQueue)new ArrayBlockingQueue<>(getQueueSizeParam());
        this
                        .eventPublisherThread = new AfterSaveEventPublisherThread(this, tenant, this.queue, this.listeners, getSleepTimeParam(), Math.min(getQueueSizeParam(), getBatchSizeParam()), getActiveTimeParam());
        this.eventPublisherThread.startAndWaitForThreadToBeRunning();
    }


    protected int getBatchSizeParam()
    {
        return Config.getInt("core.aftersave.batchsize", 1024);
    }


    protected long getSleepTimeParam()
    {
        return Config.getLong("core.aftersave.interval", 200L);
    }


    protected int getQueueSizeParam()
    {
        return Config.getInt("core.aftersave.queuesize", 1024);
    }


    protected boolean isAsyncParam()
    {
        return Config.getBoolean("core.aftersave.async", true);
    }


    protected long getActiveTimeParam()
    {
        return Config.getInt("core.aftersave.active.time", DEFAULT_ACTIVE_TIME);
    }


    protected void doNotifyListeners(Collection<AfterSaveEvent> events, Collection<AfterSaveListener> listeners)
    {
        for(AfterSaveListener listener : listeners)
        {
            try
            {
                listener.afterSave(events);
            }
            catch(Exception e)
            {
                LOG.error("error notifying AfterSaveEvent listener " + listener, e);
            }
        }
    }


    protected boolean isPublishAsynchronously()
    {
        return this.publishAsynchronously;
    }
}
