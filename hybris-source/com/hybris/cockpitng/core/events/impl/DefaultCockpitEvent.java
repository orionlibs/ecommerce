/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.events.impl;

import com.google.common.collect.Maps;
import com.hybris.cockpitng.core.events.CockpitEvent;
import java.util.Map;

public class DefaultCockpitEvent implements CockpitEvent
{
    private final String name;
    private final Object data;
    private final Object source;
    private Map<String, Object> context;


    public DefaultCockpitEvent(final String name, final Object data, final Object source)
    {
        super();
        this.name = name;
        this.data = data;
        this.source = source;
    }


    public Map<String, Object> getContext()
    {
        if(context == null)
        {
            context = Maps.newHashMap();
        }
        return context;
    }


    @Override
    public String getName()
    {
        return name;
    }


    @Override
    public Object getData()
    {
        return data;
    }


    @Override
    public Object getSource()
    {
        return source;
    }
}
