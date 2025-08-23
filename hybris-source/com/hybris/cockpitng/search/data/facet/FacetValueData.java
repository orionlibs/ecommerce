/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.search.data.facet;

import java.util.Objects;

/**
 * This class defines the contents of a facet. For example, <b>cpu</b> and <b>monitor</b> are FacetValueData of the
 * <b>hardware</b> FacetData. Each FacetValueData contains concrete items???(either subFacetValue or items)
 */
public class FacetValueData implements Comparable<FacetValueData>
{
    public static final long BREADCRUMB_COUNT = -1L;
    private final String name;
    private final String displayName;
    private final long count;
    private final boolean selected;


    public FacetValueData(final String name, final long count, final boolean selected)
    {
        this(name, name, count, selected);
    }


    public FacetValueData(final String name, final String displayName, final long count, final boolean selected)
    {
        this.name = name;
        this.displayName = displayName;
        this.count = count;
        this.selected = selected;
    }


    public String getName()
    {
        return name;
    }


    public String getDisplayName()
    {
        return displayName;
    }


    public long getCount()
    {
        return count;
    }


    public boolean isSelected()
    {
        return selected;
    }


    @Override
    public int compareTo(final FacetValueData anotherFacetValue)
    {
        return name.compareTo(anotherFacetValue.name);
    }


    @Override
    public boolean equals(final Object obj)
    {
        if(this == obj)
        {
            return true;
        }
        if(obj == null)
        {
            return false;
        }
        if(obj.getClass() != this.getClass())
        {
            return false;
        }
        final FacetValueData that = (FacetValueData)obj;
        return Objects.equals(this.name, that.name) && Objects.equals(this.displayName, that.displayName)
                        && Objects.equals(this.count, that.count) && (selected == that.selected);
    }


    @Override
    public int hashCode()
    {
        return Objects.hash(this.name, this.displayName, this.count);
    }


    @Override
    public String toString()
    {
        return getClass().getName() + " [" + name + " (" + count + ")]";
    }
}
