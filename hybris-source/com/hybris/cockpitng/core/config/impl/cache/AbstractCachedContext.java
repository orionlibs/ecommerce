/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl.cache;

import com.hybris.cockpitng.core.config.impl.jaxb.Context;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;

abstract class AbstractCachedContext extends Context implements CachedContext
{
    private final Context original;


    protected AbstractCachedContext(final Context original)
    {
        this.original = original;
    }


    @Override
    public Context getOriginal()
    {
        return original;
    }


    protected List<Context> getCachedContext()
    {
        return super.getContext();
    }


    @Override
    public Object getAny()
    {
        return original.getAny();
    }


    @Override
    public void setAny(final Object value)
    {
        original.setAny(value);
    }


    @Override
    public String getMergeBy()
    {
        return original.getMergeBy();
    }


    @Override
    public void setMergeBy(final String value)
    {
        original.setMergeBy(value);
    }


    @Override
    public String getParent()
    {
        return original.getParent();
    }


    @Override
    public void setParent(final String value)
    {
        original.setParent(value);
    }


    @Override
    public String getType()
    {
        return original.getType();
    }


    @Override
    public void setType(final String value)
    {
        original.setType(value);
    }


    @Override
    public String getPrincipal()
    {
        return original.getPrincipal();
    }


    @Override
    public void setPrincipal(final String value)
    {
        original.setPrincipal(value);
    }


    @Override
    public String getComponent()
    {
        return original.getComponent();
    }


    @Override
    public void setComponent(final String value)
    {
        original.setComponent(value);
    }


    @Override
    public Map<QName, String> getOtherAttributes()
    {
        return original.getOtherAttributes();
    }


    @Override
    public boolean equals(final Object o)
    {
        return o != null && (o.getClass() == this.getClass()) && ((AbstractCachedContext)o).original == this.original;
    }


    @Override
    public int hashCode()
    {
        return System.identityHashCode(this.original);
    }
}
