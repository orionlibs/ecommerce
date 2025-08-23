/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.util.cache;

import com.google.common.base.Stopwatch;
import com.hybris.cockpitng.core.async.Progress;
import com.hybris.cockpitng.core.async.impl.AbstractOperation;
import com.hybris.cockpitng.core.async.impl.DefaultProgress;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;
import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.IdentityHashSet;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;

/**
 * A asynchronous warm up cache that is bound to particular widget instance.
 */
public class WidgetAsyncWarmUpCache<K, V> implements AsyncWarmUpCache<K, V>
{
    private static final Logger LOG = LoggerFactory.getLogger(WidgetAsyncWarmUpCache.class);
    private static final long CHECK_INTERVAL_CANCEL_REQUEST = 250L;
    private final Set<WarmUpOperation> operations = new IdentityHashSet<>();
    private final WidgetInstanceManager wim;
    private final Map<K, V> cache;
    private EventListener<Event> callbackEvent;


    public WidgetAsyncWarmUpCache(final WidgetInstanceManager wim)
    {
        this.wim = wim;
        this.cache = Collections.synchronizedMap(new HashMap<>());
    }


    protected EventListener<Event> getCallbackEvent()
    {
        return callbackEvent;
    }


    public void setCallbackEvent(final EventListener<Event> callbackEvent)
    {
        this.callbackEvent = callbackEvent;
    }


    protected WidgetInstanceManager getWidgetInstanceManager()
    {
        return wim;
    }


    @Override
    public void warmUp(final Iterator<Collection<K>> keys, final Function<K, V> values)
    {
        final WarmUpOperation operation = createOperation(keys, values);
        registerWarmingUpOperation(operation);
        getWidgetInstanceManager().executeOperationInParallel(operation, getCallbackEvent());
    }


    protected WarmUpOperation createOperation(final Iterator<Collection<K>> keys, final Function<K, V> values)
    {
        return new WarmUpOperation(keys, values);
    }


    @Override
    public boolean has(final K key)
    {
        return cache.containsKey(key);
    }


    @Override
    public V get(final K key)
    {
        return cache.get(key);
    }


    @Override
    public V get(final K key, final Supplier<V> defaultValue)
    {
        return cache.computeIfAbsent(key, k -> defaultValue.get());
    }


    @Override
    public void revoke(final K key)
    {
        cache.remove(key);
    }


    @Override
    public void clear()
    {
        cancelRunningWarmingUp();
        cache.clear();
    }


    protected synchronized void registerWarmingUpOperation(final WarmUpOperation operation)
    {
        operations.add(operation);
    }


    protected synchronized void unregisterWarmingUpOperation(final WarmUpOperation operation)
    {
        operations.remove(operation);
    }


    protected synchronized void cancelRunningWarmingUp()
    {
        Set.copyOf(operations).forEach(WarmUpOperation::cancel);
    }


    protected class WarmUpOperation extends AbstractOperation
    {
        private final Iterator<Collection<K>> keys;
        private final Function<K, V> values;
        private final Queue<K> keySequence = new CircularFifoQueue<>(100);
        private final Map<K, Object> processedKeys = new IdentityHashMap<>();
        private Progress workingProgress;


        public WarmUpOperation(final Iterator<Collection<K>> keys, final Function<K, V> values)
        {
            super(null, true, Progress.ProgressType.NONE);
            this.keys = keys;
            this.values = values;
            this.workingProgress = new DefaultProgress();
        }


        @Override
        public Object execute(final Progress progress)
        {
            LOG.debug("Asynchronous cache warm up started");
            synchronized(WidgetAsyncWarmUpCache.this)
            {
                if(workingProgress.isCancelRequested())
                {
                    finish();
                    return null;
                }
                workingProgress = progress;
            }
            try
            {
                final Stopwatch stopwatch = Stopwatch.createStarted();
                int counter = 0;
                while(!progress.isCancelRequested() && keys.hasNext())
                {
                    final Iterator<K> ks = this.keys.next().iterator();
                    while(ks.hasNext() && !progress.isCancelRequested())
                    {
                        processKey(ks.next());
                        counter++;
                    }
                }
                stopwatch.stop();
                LOG.debug("Asynchronous cache warmed up in {}ms with {} keys processed", stopwatch.elapsed(TimeUnit.MILLISECONDS),
                                counter);
            }
            finally
            {
                finish();
            }
            return Collections.unmodifiableMap(cache);
        }


        protected void processKey(final K key)
        {
            keySequence.add(key);
            if(processedKeys.put(key, StringUtils.EMPTY) != null)
            {
                LOG.error("Unable to warm up cache due to infinite keys iterators.\r\nInfinite loop detected for key: {}\r\nTurn on DEBUG level to see last processed keys", key);
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Last up to 100 processed keys:\r\n    - {}", StringUtils.join(keySequence, "\r\n    - "));
                }
                workingProgress.requestCancel();
            }
            else
            {
                cache.put(key, values.apply(key));
            }
        }


        protected void finish()
        {
            unregisterWarmingUpOperation(this);
            synchronized(WidgetAsyncWarmUpCache.this)
            {
                workingProgress = null;
                WidgetAsyncWarmUpCache.this.notifyAll();
            }
        }


        public void cancel()
        {
            synchronized(WidgetAsyncWarmUpCache.this)
            {
                if(workingProgress != null)
                {
                    workingProgress.requestCancel();
                    while(workingProgress != null)
                    {
                        try
                        {
                            WidgetAsyncWarmUpCache.this.wait(CHECK_INTERVAL_CANCEL_REQUEST);
                        }
                        catch(final InterruptedException e)
                        {
                            Thread.currentThread().interrupt();
                            LOG.error(e.getLocalizedMessage(), e);
                        }
                    }
                }
            }
        }
    }
}
