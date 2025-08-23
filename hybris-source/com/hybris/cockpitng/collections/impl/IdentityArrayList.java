/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.collections.impl;

import com.hybris.cockpitng.collections.IdentityList;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Default implementation for {@link IdentityList}
 */
public class IdentityArrayList<E> extends ArrayList<E> implements IdentityList<E>
{
    public IdentityArrayList()
    {
        super();
    }


    /**
     * @deprecated since 1811. Not used anymore.
     */
    @Deprecated(since = "1811", forRemoval = true)
    public IdentityArrayList(final Collection<? extends E> collection)
    {
        super(collection);
    }


    /**
     * @deprecated since 1811. Not used anymore.
     */
    @Deprecated(since = "1811", forRemoval = true)
    public IdentityArrayList(final int initialCapacity)
    {
        super(initialCapacity);
    }


    @Override
    public boolean contains(final Object object)
    {
        return indexOf(object) >= 0;
    }


    @Override
    public int indexOf(final Object object)
    {
        for(int i = 0; i < size(); i++)
        {
            if(object == get(i))
            {
                return i;
            }
        }
        return -1;
    }


    @Override
    public int lastIndexOf(final Object object)
    {
        for(int i = size() - 1; i >= 0; i--)
        {
            if(object == get(i))
            {
                return i;
            }
        }
        return -1;
    }
}
