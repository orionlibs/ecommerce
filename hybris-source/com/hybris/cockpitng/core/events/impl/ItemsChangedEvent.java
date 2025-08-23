/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.events.impl;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public class ItemsChangedEvent extends DefaultCockpitEvent
{
    public static final String NAME = "cockpitItemChangedEvent";
    private final Set<Object> items;


    public ItemsChangedEvent(final Collection<?> items, final Object source)
    {
        super(NAME, items, source);
        this.items = new LinkedHashSet<Object>(items);
    }


    @Override
    public String getName()
    {
        return NAME;
    }


    @Override
    public Object getData()
    {
        return items;
    }


    public Set<Object> getItems()
    {
        return items;
    }


    public <T> Set<T> getItemsOfType(final Class<T> clazz)
    {
        final Set<T> ret = new LinkedHashSet<T>();
        for(final Object item : items)
        {
            if(clazz.isAssignableFrom(item.getClass()))
            {
                ret.add((T)item);
            }
        }
        return ret;
    }
}
