/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.context.impl;

import com.hybris.cockpitng.core.context.CockpitContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Default <code>CockpitContext</code> implementation.
 */
public class DefaultCockpitContext implements CockpitContext
{
    private final Map<String, Object> parameters = new HashMap<>();


    public DefaultCockpitContext()
    {
    }


    public DefaultCockpitContext(final Map<String, Object> parameters)
    {
        if(parameters != null)
        {
            this.parameters.putAll(parameters);
        }
    }


    @Override
    public Map<String, Object> getParameters()
    {
        return parameters;
    }


    @Override
    public void setParameters(final Map<String, Object> parameters)
    {
        this.parameters.clear();
        this.parameters.putAll(parameters);
    }


    @Override
    public void setParameter(final String key, final Object value)
    {
        parameters.put(key, value);
    }


    @Override
    public Object getParameter(final String key)
    {
        return parameters.get(key);
    }


    @Override
    public void removeParameter(final String key)
    {
        parameters.remove(key);
    }


    @Override
    public Set<String> getParameterKeys()
    {
        return Collections.unmodifiableSet(parameters.keySet());
    }


    @Override
    public boolean containsParameter(final String key)
    {
        return parameters.containsKey(key);
    }


    @Override
    public void clearParameters()
    {
        parameters.clear();
    }


    @Override
    public void addAllParameters(final CockpitContext context)
    {
        if(context != null)
        {
            context.getParameters().forEach(this::setParameter);
        }
    }
}
