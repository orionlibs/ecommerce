/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.model.impl;

import com.hybris.cockpitng.core.model.Observable;
import com.hybris.cockpitng.core.model.ValueObserver;
import com.hybris.cockpitng.core.util.ObjectValuePath;
import java.lang.ref.WeakReference;

/**
 * A proxy observer to pass model change events with some prefix. Whenever a source model changes, destination model is
 * asked to notify all its observers about a change, but with prefix prepended.
 * <P>
 * Observer is available in two versions:
 * <ul>
 * <li>destination model may be referenced strongly: this proxy should be used whenever a link between source and</li>
 * <li>destination is permanent and should last as long both of them exist</li>
 * <li>destination model may be referenced weekly: this proxy should be used whenever a link between source and</li>
 * <li>destination is temporary and should not prevent GC from collecting destination model</li>
 * </ul>
 */
public class ObserverProxy implements ValueObserver
{
    /**
     * Creates a proxy observer with strong reference to destination
     *
     * @param destination
     *           destination model
     * @param prefix
     *           prefix added to notification of destination
     * @return prefix observer ready to be registered in source model
     */
    public static ValueObserver createProxy(final Observable destination, final ObjectValuePath prefix)
    {
        return new ObserverProxy(new StrongReferenceProvider(destination), prefix);
    }


    /**
     * Creates a proxy observer with weak reference to destination
     *
     * @param destination
     *           destination model
     * @param prefix
     *           prefix added to notification of destination
     * @return prefix observer ready to be registered in source model
     */
    public static ValueObserver createWeakProxy(final Observable destination, final ObjectValuePath prefix)
    {
        return new ObserverProxy(new WeakReferenceProvider(destination), prefix);
    }


    private final Provider destination;
    private final ObjectValuePath prefix;


    private ObserverProxy(final Provider destination, final ObjectValuePath prefix)
    {
        this.destination = destination;
        this.prefix = prefix;
    }


    @Override
    public void modelChanged()
    {
        final Observable dest = destination.get();
        if(dest != null)
        {
            dest.changed(prefix.buildPath());
        }
    }


    @Override
    public void modelChanged(final String property)
    {
        final Observable dest = destination.get();
        if(dest != null)
        {
            dest.changed(ObjectValuePath.copy(prefix).appendPath(property).buildPath());
        }
    }


    private interface Provider
    {
        Observable get();
    }


    private static class WeakReferenceProvider implements Provider
    {
        private final WeakReference<Observable> value;


        private WeakReferenceProvider(final Observable value)
        {
            this.value = new WeakReference<>(value);
        }


        @Override
        public Observable get()
        {
            return this.value.get();
        }
    }


    private static class StrongReferenceProvider implements Provider
    {
        private final Observable value;


        private StrongReferenceProvider(final Observable value)
        {
            this.value = value;
        }


        @Override
        public Observable get()
        {
            return this.value;
        }
    }
}
