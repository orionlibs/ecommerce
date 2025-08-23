/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.extendedmultireferenceeditor.state;

public class ListheaderSortState
{
    private final String label;
    private final boolean ascending;


    public ListheaderSortState(final String label, final boolean ascending)
    {
        this.label = label;
        this.ascending = ascending;
    }


    public String getLabel()
    {
        return label;
    }


    public boolean isAscending()
    {
        return ascending;
    }


    @Override
    public boolean equals(final Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null || getClass() != o.getClass())
        {
            return false;
        }
        final ListheaderSortState that = (ListheaderSortState)o;
        if(ascending != that.ascending)
        {
            return false;
        }
        return label != null ? label.equals(that.label) : that.label == null;
    }


    @Override
    public int hashCode()
    {
        int result = label != null ? label.hashCode() : 0;
        result = 31 * result + (ascending ? 1 : 0);
        return result;
    }


    @Override
    public String toString()
    {
        return "ListheaderSortState{" + "label='" + label + '\'' + ", ascending=" + ascending + '}';
    }
}
