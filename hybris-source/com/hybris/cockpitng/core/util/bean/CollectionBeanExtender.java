/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.util.bean;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.springframework.beans.factory.BeanInitializationException;

/**
 * Modification bean to change contents of a collection property of specified bean from any spring contexts in hierarchy
 */
public class CollectionBeanExtender extends AbstractBeanExtender
{
    public static final String BEAN_NAME = "collectionExtender";
    private Collection<Object> add;
    private Collection<Object> remove;
    private boolean unique;


    public CollectionBeanExtender(final String extendedBeanName)
    {
        super(extendedBeanName);
    }


    @Override
    protected void checkProperty(final Object target)
    {
        if(target == null)
        {
            throw new BeanInitializationException("Unable to extend null collection");
        }
        checkPropertyType(target, Collection.class);
    }


    @Override
    protected Object modifyProperty(final Object target)
    {
        final Collection<Object> collection = (Collection<Object>)target;
        if(getAdd() != null)
        {
            collection.addAll(getAdd());
        }
        if(getRemove() != null)
        {
            collection.removeAll(getRemove());
        }
        if(isUnique())
        {
            makeUnique(collection);
        }
        return collection;
    }


    protected void makeUnique(final Collection<Object> collection)
    {
        final Set<Object> check = new HashSet();
        for(final Iterator<Object> it = collection.iterator(); it.hasNext(); )
        {
            final Object next = it.next();
            if(check.contains(next))
            {
                it.remove();
            }
            else
            {
                check.add(next);
            }
        }
    }


    protected Collection<Object> getAdd()
    {
        return add;
    }


    public void setAdd(final Collection<Object> add)
    {
        this.add = add;
    }


    protected Collection<Object> getRemove()
    {
        return remove;
    }


    public void setRemove(final Collection<Object> remove)
    {
        this.remove = remove;
    }


    public boolean isUnique()
    {
        return unique;
    }


    public void setUnique(final boolean unique)
    {
        this.unique = unique;
    }
}
