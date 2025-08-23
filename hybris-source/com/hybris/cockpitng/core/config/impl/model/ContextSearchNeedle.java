/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl.model;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Single context search requirements
 */
public class ContextSearchNeedle
{
    private final Map<String, String> attributesNeedle;


    public ContextSearchNeedle()
    {
        this(Collections.emptyMap());
    }


    public ContextSearchNeedle(final Map<String, String> attributesNeedle)
    {
        this.attributesNeedle = new LinkedHashMap<>(attributesNeedle != null ? attributesNeedle : Collections.emptyMap());
    }


    /**
     * Gets attributes requested by needle
     *
     * @return requested attribute values divided by their name
     */
    public Map<String, String> getAttributes()
    {
        return Collections.unmodifiableMap(attributesNeedle);
    }


    @Override
    public boolean equals(final Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null)
        {
            return false;
        }
        if(o.getClass() != this.getClass())
        {
            return false;
        }
        final ContextSearchNeedle that = (ContextSearchNeedle)o;
        return attributesNeedle.equals(that.attributesNeedle);
    }


    @Override
    public int hashCode()
    {
        return attributesNeedle.hashCode();
    }
}
