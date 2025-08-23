/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.impl;

import com.google.common.collect.Lists;
import com.hybris.cockpitng.core.model.Identifiable;
import com.hybris.cockpitng.core.model.ModelValueHandler;
import com.hybris.cockpitng.core.model.Observable;
import com.hybris.cockpitng.core.model.ValueObserver;
import com.hybris.cockpitng.core.model.WidgetModel;
import com.hybris.cockpitng.core.model.impl.AbstractObservable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultWidgetModel extends AbstractObservable implements WidgetModel, Map<String, Object>
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultWidgetModel.class);
    private static final String REGISTERED_OBSERVABLE_VALUE_OBSERVERS = "_registerdObservableValueObservers";
    private final transient Map<String, Object> realModel;
    private final transient ModelValueHandler valueHandler;
    private final transient ThreadLocal<Boolean> notifyMapOperations = new ThreadLocal<>();


    public DefaultWidgetModel(final Map<String, Object> realModel, final ModelValueHandler valueHandler)
    {
        this.realModel = realModel;
        this.valueHandler = valueHandler;
        this.realModel.forEach(this::updateValueObserver);
    }


    @Override
    public Object put(final String attribute, final Object value)
    {
        final Object ret;
        if(value == null)
        {
            ret = this.realModel.remove(attribute);
            if(ret != null)
            {
                removeValueObserver(attribute, ret);
            }
        }
        else
        {
            ret = this.realModel.put(attribute, value);
            if(ret != value && ret != null)
            {
                removeValueObserver(attribute, ret);
            }
            updateValueObserver(attribute, value);
        }
        if(isNotifyMapOperations())
        {
            changed(attribute);
        }
        return ret;
    }


    @Override
    public Object remove(final String attribute)
    {
        final Object result = this.realModel.remove(attribute);
        if(isNotifyMapOperations())
        {
            changed(attribute);
        }
        return result;
    }


    private boolean isNotifyMapOperations()
    {
        return BooleanUtils.toBooleanDefaultIfNull(this.notifyMapOperations.get(), true);
    }


    @Override
    public <T> T getValue(final String expression, final Class<T> expectedValueType)
    {
        final Object value = this.valueHandler.getValue(this, expression);
        if(value == null)
        {
            return null;
        }
        else
        {
            if(expectedValueType.isAssignableFrom(value.getClass()))
            {
                return (T)value;
            }
            else
            {
                LOG.error("type mismatch, model attribute '{}' not an instance of '{}' but '{}'", expression,
                                expectedValueType.getName(), value.getClass().getName());
                return null;
            }
        }
    }


    @Override
    public void setValue(final String expression, final Object value)
    {
        try
        {
            this.notifyMapOperations.set(Boolean.FALSE);
            this.valueHandler.setValue(this, expression, value);
        }
        finally
        {
            this.notifyMapOperations.remove();
        }
        changed(expression);
    }


    @Override
    public <T> Class<T> getValueType(final String expression)
    {
        return this.valueHandler.getValueType(this, expression);
    }


    @Override
    public void removeObserver(final String key, final Object observerId)
    {
        synchronized(this.mutex)
        {
            getObservers(key).stream().filter(
                                            observer -> (observer instanceof Identifiable) && Objects.equals(observerId, ((Identifiable)observer).getId()))
                            .forEach(observer -> removeObserver(key, observer));
        }
    }


    @Override
    public void removeObserver(final Object observerId)
    {
        synchronized(this.mutex)
        {
            getObservedValues().forEach(key -> removeObserver(key, observerId));
        }
    }


    @Override
    public void removeAllObservers()
    {
        synchronized(this.mutex)
        {
            getObservedValues().stream().map(this::getObservers).flatMap(Collection::stream).distinct()
                            .forEach(this::removeObserver);
        }
    }


    @Override
    public int size()
    {
        return realModel.size();
    }


    @Override
    public boolean isEmpty()
    {
        return realModel.isEmpty();
    }


    @Override
    public boolean containsKey(final Object key)
    {
        return realModel.containsKey(key);
    }


    @Override
    public boolean containsValue(final Object value)
    {
        return realModel.containsValue(value);
    }


    @Override
    public Object get(final Object key)
    {
        return realModel.get(key);
    }


    @Override
    public Object remove(final Object key)
    {
        final Object result = realModel.remove(key);
        if(key instanceof String && isNotifyMapOperations())
        {
            changed((String)key);
        }
        return result;
    }


    @Override
    public void putAll(final Map<? extends String, ?> map)
    {
        if(map != null)
        {
            for(final Map.Entry<? extends String, ?> entry : map.entrySet())
            {
                put(entry.getKey(), entry.getValue());
            }
        }
    }


    @Override
    public void clear()
    {
        realModel.clear();
        changed();
    }


    @Override
    public Set<String> keySet()
    {
        return realModel.keySet();
    }


    @Override
    public Collection<Object> values()
    {
        return realModel.values();
    }


    @Override
    public Set<java.util.Map.Entry<String, Object>> entrySet()
    {
        return realModel.entrySet();
    }


    private void removeValueObserver(final String attribute, final Object givenValue)
    {
        if(givenValue instanceof Observable)
        {
            removeValueObserver(attribute, (Observable)givenValue);
        }
    }


    private void removeValueObserver(final String attribute, final Observable observable)
    {
        final Map<String, List<ValueObserver>> currentObservers = getRegisteredValueObservers();
        if(currentObservers == null)
        {
            return;
        }
        final List<ValueObserver> valueObservers = currentObservers.get(attribute);
        if(CollectionUtils.isEmpty(valueObservers))
        {
            return;
        }
        for(final ValueObserver observer : valueObservers)
        {
            if(observer != null)
            {
                observable.removeObserver(observer);
            }
        }
        valueObservers.clear();
    }


    private Map<String, List<ValueObserver>> getRegisteredValueObservers()
    {
        return (Map<String, List<ValueObserver>>)this.realModel.get(REGISTERED_OBSERVABLE_VALUE_OBSERVERS);
    }


    private void registerValueObserver(final String key, final ValueObserver valueObserver, final Observable observable)
    {
        Map<String, List<ValueObserver>> valueObservers = getRegisteredValueObservers();
        if(valueObservers == null)
        {
            valueObservers = new ConcurrentHashMap<>();
            realModel.put(REGISTERED_OBSERVABLE_VALUE_OBSERVERS, valueObservers);
        }
        observable.addObserver(valueObserver);
        if(valueObservers.containsKey(key))
        {
            valueObservers.get(key).add(valueObserver);
        }
        else
        {
            valueObservers.put(key, Lists.newArrayList(valueObserver));
        }
    }


    protected void updateValueObserver(final String attribute, final Object value)
    {
        if(value instanceof Observable)
        {
            final Observable observable = (Observable)value;
            final ValueObserver valueObserver = new ValueObserver()
            {
                @Override
                public void modelChanged()
                {
                    changed(attribute);
                }


                @Override
                public void modelChanged(final String property)
                {
                    changed(String.format("%s.%s", attribute, property));
                }
            };
            removeValueObserver(attribute, observable);
            registerValueObserver(attribute, valueObserver, observable);
        }
    }
}
