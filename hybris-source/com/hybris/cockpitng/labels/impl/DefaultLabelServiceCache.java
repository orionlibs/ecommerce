/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.labels.impl;

import com.hybris.cockpitng.labels.LabelStringObjectHandler;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Required;

/**
 * Default implementation of label service cache that uses {@link HashedMap} to persist values.
 */
public class DefaultLabelServiceCache implements LabelServiceCache
{
    private final Map<Pair<Object, Locale>, String> labels = new ConcurrentHashMap<>();
    private final Map<Pair<Object, Locale>, String> shortLabels = new ConcurrentHashMap<>();
    private final Map<Pair<Object, Locale>, String> descriptions = new ConcurrentHashMap<>();
    private final Map<Pair<Object, Locale>, String> objectIconPath = new ConcurrentHashMap<>();
    private LabelStringObjectHandler labelStringObjectHandler;


    @Override
    public String getObjectLabel(final Object object, final Supplier<String> defaultValue)
    {
        return computeIfAbsent(labels, object, defaultValue);
    }


    @Override
    public String getShortObjectLabel(final Object object, final Supplier<String> defaultValue)
    {
        return computeIfAbsent(shortLabels, object, defaultValue);
    }


    @Override
    public String getObjectDescription(final Object object, final Supplier<String> defaultValue)
    {
        return computeIfAbsent(descriptions, object, defaultValue);
    }


    @Override
    public String getObjectIconPath(final Object object, final Supplier<String> defaultValue)
    {
        return computeIfAbsent(objectIconPath, object, defaultValue);
    }


    protected String computeIfAbsent(final Map<Pair<Object, Locale>, String> map, final Object object,
                    final Supplier<String> defaultValue)
    {
        final ImmutablePair<Object, Locale> key = ImmutablePair.of(object, getLabelStringObjectHandler().getCurrentLocale());
        if(!map.containsKey(key))
        {
            final String value = Optional.ofNullable(defaultValue.get()).orElse(StringUtils.EMPTY);
            map.put(key, value);
            return value;
        }
        else
        {
            return map.get(key);
        }
    }


    protected LabelStringObjectHandler getLabelStringObjectHandler()
    {
        return labelStringObjectHandler;
    }


    @Required
    public void setLabelStringObjectHandler(final LabelStringObjectHandler labelStringObjectHandler)
    {
        this.labelStringObjectHandler = labelStringObjectHandler;
    }
}
