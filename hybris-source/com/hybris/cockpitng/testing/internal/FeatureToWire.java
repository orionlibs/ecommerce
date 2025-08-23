/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.testing.internal;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class FeatureToWire
{
    private final Class<?> type;
    private final String qualifier;
    private final Object toWire;


    public FeatureToWire(final Class<?> type, final Object toWire)
    {
        this(type, null, toWire);
    }


    public FeatureToWire(final Class<?> type, final String qualifier, final Object toWire)
    {
        this.type = type;
        this.qualifier = qualifier;
        this.toWire = toWire;
    }


    public Class<?> getType()
    {
        return type;
    }


    public String getQualifier()
    {
        return qualifier;
    }


    public Object getToWire()
    {
        return toWire;
    }


    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
}
