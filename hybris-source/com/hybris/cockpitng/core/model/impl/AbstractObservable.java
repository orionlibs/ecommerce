/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.model.impl;

import com.hybris.cockpitng.core.model.Identifiable;
import com.hybris.cockpitng.core.model.Observable;
import com.hybris.cockpitng.core.model.ValueObserver;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

public class AbstractObservable implements Observable
{
    protected final Object mutex = new Object();
    private final Map<String, List<ValueObserver>> observerMap = new LinkedHashMap<>();


    private static boolean isDelimiterChar(final char character)
    {
        return !Character.isUnicodeIdentifierPart(character);
    }


    private static boolean startsWithKey(final String key1, final String key2)
    {
        return key1.equals(key2) || key1.startsWith(key2) && isDelimiterChar(key1.charAt(key2.length()));
    }


    protected Set<String> getObservedValues()
    {
        return Collections.unmodifiableSet(observerMap.keySet());
    }


    protected List<ValueObserver> getObservers(final String key)
    {
        synchronized(this.mutex)
        {
            final List<ValueObserver> result = new ArrayList<>();
            final Set<String> keySet = observerMap.keySet();
            for(final String mapKey : keySet)
            {
                if(startsWithKey(mapKey, key) || StringUtils.isEmpty(mapKey) || startsWithKey(key, mapKey))
                {
                    final List<ValueObserver> listeners = observerMap.get(mapKey);
                    if(listeners != null && !listeners.isEmpty())
                    {
                        result.addAll(listeners);
                    }
                }
            }
            return Collections.unmodifiableList(result);
        }
    }


    @Override
    public void changed()
    {
        final Set<ValueObserver> observers;
        synchronized(mutex)
        {
            observers = getObservedValues().stream().map(this::getObservers).flatMap(os -> new HashSet<>(os).stream())
                            .collect(Collectors.toSet());
        }
        observers.forEach(ValueObserver::modelChanged);
    }


    @Override
    public void changed(final String key)
    {
        final List<ValueObserver> observers = getObservers(key);
        observers.forEach(observer -> observer.modelChanged(key));
    }


    protected boolean alreadyExists(final ValueObserver observer, final List<ValueObserver> observers)
    {
        if(observer instanceof Identifiable)
        {
            final Identifiable modelObserver = (Identifiable)observer;
            final Object id = modelObserver.getId();
            return id != null && observers.stream().anyMatch(
                            o -> o instanceof Identifiable ? Objects.equals(((Identifiable)o).getId(), id) : Objects.equals(o, observer));
        }
        else
        {
            return observers.contains(observer);
        }
    }


    @Override
    public void addObserver(final String key, final ValueObserver observer)
    {
        synchronized(this.mutex)
        {
            final List<ValueObserver> observers = observerMap.computeIfAbsent(key, k -> new ArrayList<>(1));
            if(!alreadyExists(observer, observers))
            {
                observers.add(observer);
            }
        }
    }


    @Override
    public void removeObserver(final String key, final ValueObserver observer)
    {
        synchronized(this.mutex)
        {
            final List<ValueObserver> observers = observerMap.get(key);
            if(observers != null)
            {
                observers.remove(observer);
            }
        }
    }


    @Override
    public void removeObserver(final ValueObserver observer)
    {
        synchronized(this.mutex)
        {
            observerMap.values().stream().filter(Objects::nonNull).forEach(observers -> observers.remove(observer));
        }
    }
}
