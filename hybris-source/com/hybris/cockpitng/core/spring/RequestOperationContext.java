/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.spring;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/**
 * Context for scope with destruction callbacks
 *
 * @deprecated since 2105, not used any more
 */
@Deprecated(since = "2105", forRemoval = true)
public class RequestOperationContext
{
    private final Map<String, Runnable> beanDestructionCallbacks = new ConcurrentHashMap<>();
    private final Map<String, Object> beans = new ConcurrentHashMap<>();
    private final Object requestId;
    private final AtomicInteger usages = new AtomicInteger();


    protected RequestOperationContext()
    {
        this.requestId = RequestOperationContextManager.getRequestId();
    }


    public <T> T getBean(final String beanName, final Supplier<T> beanSupplier)
    {
        return (T)beans.computeIfAbsent(beanName, name -> beanSupplier.get());
    }


    public <T> T removeBean(final String beanName)
    {
        final Object bean = beans.remove(beanName);
        final Runnable callback = beanDestructionCallbacks.remove(beanName);
        if(callback != null)
        {
            callback.run();
        }
        return (T)bean;
    }


    /**
     * Register bean destruction callback
     *
     * @param beanName
     *           bean name
     * @param callback
     *           callback to run during bean destruction
     */
    public void registerDestructionCallback(final String beanName, final Runnable callback)
    {
        beanDestructionCallbacks.put(beanName, callback);
    }


    protected Object getRequestId()
    {
        return requestId;
    }


    protected void registerUsage()
    {
        usages.incrementAndGet();
    }


    protected void unregisterUsage()
    {
        if(usages.decrementAndGet() == 0)
        {
            finalizeContext();
        }
    }


    protected void finalizeContext()
    {
        beans.clear();
        final HashMap<String, Runnable> callbacks = new HashMap<>(beanDestructionCallbacks);
        beanDestructionCallbacks.clear();
        callbacks.values().forEach(Runnable::run);
    }
}
