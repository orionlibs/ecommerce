/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl;

import com.hybris.cockpitng.core.config.ConfigContext;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class DefaultConfigContext implements ConfigContext
{
    public static final String CONTEXT_COMPONENT = "component";
    public static final String CONTEXT_SUBCOMPONENT = "subcomponent";
    public static final String CONTEXT_TYPE = "type";
    public static final String CONTEXT_PRINCIPAL = "principal";
    public static final String CONTEXT_AUTHORITY = "authority";
    public static final int INITIAL_CAPACITY = 4;
    private final Map<String, String> attributes;


    public DefaultConfigContext()
    {
        this.attributes = new LinkedHashMap<>(INITIAL_CAPACITY);
    }


    public DefaultConfigContext(final String component)
    {
        this();
        setComponent(component);
    }


    public DefaultConfigContext(final String component, final String type)
    {
        this(component);
        setType(type);
    }


    public DefaultConfigContext(final String component, final String type, final String principal)
    {
        this(component, type);
        setPrincipal(principal);
    }


    public final void setComponent(final String value)
    {
        this.attributes.put(CONTEXT_COMPONENT, value);
    }


    public final void setSubcomponent(final String value)
    {
        this.attributes.put(CONTEXT_SUBCOMPONENT, value);
    }


    public final void setType(final String value)
    {
        this.attributes.put(CONTEXT_TYPE, value);
    }


    public final void setPrincipal(final String value)
    {
        this.attributes.put(CONTEXT_PRINCIPAL, value);
    }


    public final void setAuthority(final String value)
    {
        this.attributes.put(CONTEXT_AUTHORITY, value);
    }


    public void addAttribute(final String key, final String value)
    {
        this.attributes.put(key, value);
    }


    @Override
    public String getAttribute(final String key)
    {
        return this.attributes.get(key);
    }


    @Override
    public Set<String> getAttributeNames()
    {
        return Collections.unmodifiableSet(this.attributes.keySet());
    }


    @Override
    public boolean equals(final Object object)
    {
        if(this == object)
        {
            return true;
        }
        if(object == null)
        {
            return false;
        }
        if(object.getClass() != this.getClass())
        {
            return false;
        }
        if(this.attributes == null)
        {
            return (((DefaultConfigContext)object).attributes == null);
        }
        return this.attributes.equals(((DefaultConfigContext)object).attributes);
    }


    @Override
    public int hashCode()
    {
        return (this.attributes == null ? super.hashCode() : this.attributes.hashCode());
    }


    @Override
    public String toString()
    {
        return "DefaultConfigContext(" + this.attributes + ")";
    }


    public static DefaultConfigContext clone(final ConfigContext context, final String newComponent, final String subComponent)
    {
        final DefaultConfigContext newContext = new DefaultConfigContext();
        context.getAttributeNames().forEach(attr -> newContext.addAttribute(attr, context.getAttribute(attr)));
        newContext.setComponent(newComponent);
        newContext.setSubcomponent(subComponent);
        return newContext;
    }
}
