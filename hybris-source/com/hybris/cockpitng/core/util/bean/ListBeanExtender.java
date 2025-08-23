/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.util.bean;

import java.util.Comparator;
import java.util.List;
import org.springframework.core.OrderComparator;
import org.springframework.core.Ordered;

/**
 * Modification bean to change contents of a list property of specified bean from any spring contexts in hierarchy
 * and/or sort them.
 */
public class ListBeanExtender extends CollectionBeanExtender
{
    public static final String BEAN_NAME = "listExtender";
    private Comparator<Object> comparator;
    private boolean sort;


    public ListBeanExtender(final String extendedBeanName)
    {
        super(extendedBeanName);
    }


    @Override
    protected void checkProperty(final Object target)
    {
        super.checkProperty(target);
        checkPropertyType(target, List.class);
    }


    @Override
    protected Object modifyProperty(final Object target)
    {
        final List<Object> collection = (List<Object>)super.modifyProperty(target);
        if(isSort())
        {
            collection.sort(getComparator());
        }
        return collection;
    }


    protected boolean isSort()
    {
        return sort;
    }


    public void setSort(final boolean sort)
    {
        this.sort = sort;
        if(sort && comparator == null)
        {
            setDefaultComparator();
        }
    }


    protected Comparator<Object> getComparator()
    {
        return comparator;
    }


    public void setComparator(final Comparator<Object> comparator)
    {
        this.comparator = comparator;
        if(comparator != null)
        {
            setSort(true);
        }
        else
        {
            setDefaultComparator();
        }
    }


    protected void setDefaultComparator()
    {
        setComparator(OrderComparator.INSTANCE.withSourceProvider(obj -> {
            if(obj instanceof Ordered)
            {
                return obj;
            }
            else
            {
                return (Ordered)() -> Ordered.LOWEST_PRECEDENCE;
            }
        }));
    }
}
