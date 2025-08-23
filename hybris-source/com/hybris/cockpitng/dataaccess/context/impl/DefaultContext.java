/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dataaccess.context.impl;

import com.hybris.cockpitng.dataaccess.context.Context;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Default {@link Context} implementation.
 */
public class DefaultContext implements Context
{
    private final Map<String, Object> internalCtx = new LinkedHashMap<>();


    @Override
    public Object getAttribute(final String name)
    {
        Object ret = null;
        if(internalCtx.containsKey(name))
        {
            ret = internalCtx.get(name);
        }
        return ret;
    }


    @Override
    public void addAttribute(final String name, final Object value)
    {
        internalCtx.put(name, value);
    }


    @Override
    public Set<String> getAttributeNames()
    {
        return Collections.unmodifiableSet(internalCtx.keySet());
    }


    @Override
    public Object removeAttribute(final String name)
    {
        return internalCtx.remove(name);
    }


    @Override
    public void clearAttributes()
    {
        internalCtx.clear();
    }


    public static class Builder
    {
        private final DefaultContext context = new DefaultContext();


        public Builder()
        {
            // NOP
        }


        public Builder(final Context context)
        {
            for(final String attribute : context.getAttributeNames())
            {
                this.context.addAttribute(attribute, context.getAttribute(attribute));
            }
        }


        public Builder attributes(final Map<String, ?> attributes)
        {
            for(final Map.Entry<String, ?> attribute : attributes.entrySet())
            {
                attribute(attribute.getKey(), attribute.getValue());
            }
            return this;
        }


        public Builder attribute(final String name, final Object value)
        {
            context.addAttribute(name, value);
            return this;
        }


        public Context build()
        {
            return context;
        }
    }
}
