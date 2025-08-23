/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl.cache;

import com.hybris.cockpitng.core.config.impl.jaxb.Context;
import java.util.AbstractList;
import java.util.Objects;

abstract class AbstractCachedContextList extends AbstractList<Context>
{
    private final AbstractCachedContext cachedContext;
    private final Context original;


    protected AbstractCachedContextList(final AbstractCachedContext cachedContext, final Context original)
    {
        this.cachedContext = cachedContext;
        this.original = original;
    }


    protected void checkRange(final int index)
    {
        if(original.getContext().size() <= index)
        {
            final String msg = "Index: " + index + ", Size: " + original.getContext().size();
            throw new IndexOutOfBoundsException(msg);
        }
        while(cachedContext.getCachedContext().size() <= index)
        {
            cachedContext.getCachedContext().add(null);
        }
    }


    @Override
    public Context get(final int index)
    {
        checkRange(index);
        if(cachedContext.getCachedContext().get(index) == null && original.getContext().get(index) != null)
        {
            cachedContext.getCachedContext().set(index, wrapCachedContext(cachedContext, original.getContext().get(index)));
        }
        return cachedContext.getCachedContext().get(index);
    }


    @Override
    public int size()
    {
        return original.getContext().size();
    }


    @Override
    public Context set(final int index, Context element)
    {
        checkRange(index);
        final Context result;
        if(element instanceof com.hybris.cockpitng.core.config.impl.cache.CachedContext)
        {
            result = cachedContext.getCachedContext().set(index, element);
            element = ((com.hybris.cockpitng.core.config.impl.cache.CachedContext)element).getOriginal();
        }
        else
        {
            result = cachedContext.getCachedContext().set(index, null);
        }
        final Context superResult = super.set(index, element);
        if(result == null && superResult != null)
        {
            return wrapCachedContext(cachedContext, superResult);
        }
        else
        {
            return result;
        }
    }


    @Override
    public void add(final int index, Context element)
    {
        if(index > 0)
        {
            checkRange(index - 1);
        }
        if(element instanceof com.hybris.cockpitng.core.config.impl.cache.CachedContext)
        {
            cachedContext.getCachedContext().add(index, element);
            element = ((com.hybris.cockpitng.core.config.impl.cache.CachedContext)element).getOriginal();
        }
        cachedContext.getCachedContext().add(index, element);
    }


    @Override
    public Context remove(final int index)
    {
        checkRange(index);
        final Context result = cachedContext.getCachedContext().remove(index);
        final Context superResult = super.remove(index);
        if(result == null && superResult != null)
        {
            return wrapCachedContext(cachedContext, superResult);
        }
        else
        {
            return result;
        }
    }


    @Override
    public boolean equals(final Object o)
    {
        return o != null && getClass().equals(o.getClass())
                        && Objects.equals(original.getContext(), ((AbstractCachedContextList)o).original.getContext());
    }


    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getOriginal());
    }


    public Context getOriginal()
    {
        return original;
    }


    protected abstract AbstractCachedContext wrapCachedContext(final AbstractCachedContext parent, final Context context);
}
