/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.collectionbrowser.mold.impl;

import com.google.common.collect.Lists;
import com.hybris.cockpitng.search.data.SortData;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;

public class SinglePage
{
    public static final SinglePage EMPTY = new SinglePage(Collections.emptyList(), StringUtils.EMPTY, new SortData(), SinglePage.EMPTY_SINGLE_PAGE_SIZE);
    private static final int DEFAULT_PAGE_SIZE = 50;
    private static final int EMPTY_SINGLE_PAGE_SIZE = 0;
    private final List<?> list;
    private final SortData sortData;
    private final int pageSize;
    private final String typeCode;


    public SinglePage(final String typeCode)
    {
        this.list = Lists.newArrayList();
        this.typeCode = typeCode;
        this.sortData = null;
        this.pageSize = SinglePage.DEFAULT_PAGE_SIZE;
    }


    public SinglePage(final List<?> list, final String typeCode, final SortData sortData, final int pageSize)
    {
        this.list = list;
        this.typeCode = typeCode;
        this.sortData = sortData;
        this.pageSize = pageSize;
    }


    public List<?> getList()
    {
        return list;
    }


    public int getListSize()
    {
        if(isListPresent())
        {
            return list.size();
        }
        return 0;
    }


    public String getTypeCode()
    {
        return typeCode;
    }


    public SortData getSortData()
    {
        return sortData;
    }


    public int getPageSize()
    {
        return pageSize;
    }


    public boolean isListPresent()
    {
        return list != null && !list.isEmpty();
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
        final SinglePage that = (SinglePage)o;
        return pageSize == that.pageSize && Objects.equals(list, that.list) && Objects.equals(sortData, that.sortData)
                        && Objects.equals(typeCode, that.typeCode);
    }


    @Override
    public int hashCode()
    {
        return Objects.hash(list, sortData, pageSize, typeCode);
    }
}
