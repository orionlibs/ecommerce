/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.list;

import com.hybris.cockpitng.search.data.pageable.Pageable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zul.AbstractListModel;
import org.zkoss.zul.event.ListDataEvent;

/**
 * List model which allows to display lazy loaded data from each page {@link Pageable}.
 */
public class LazyPageableListModel<T> extends AbstractListModel<T>
{
    private static final Logger LOG = LoggerFactory.getLogger(LazyPageableListModel.class);
    private transient final Map<Integer, List<T>> pageCache = new HashMap<>();
    private transient final Pageable<T> pageable;


    public LazyPageableListModel(final Pageable<T> pageable)
    {
        this.pageable = pageable;
    }


    @Override
    public int getSize()
    {
        return pageable.getTotalCount();
    }


    @Override
    public T getElementAt(final int index)
    {
        if(index < 0 || index >= getSize())
        {
            return null;
        }
        final int pageSize = pageable.getPageSize();
        final int effectivePage = index / pageSize;
        final int currentPageEnd = (effectivePage + 1) * pageSize;
        final int currentPageStart = currentPageEnd - pageSize;
        if(index >= currentPageStart && index < currentPageEnd)
        {
            final int effectiveIndex = index - currentPageStart;
            final List<T> currentPage = getOrLoadPage(effectivePage);
            if(currentPage.size() > effectiveIndex)
            {
                return currentPage.get(effectiveIndex);
            }
        }
        return null;
    }


    /**
     * Refreshes {@link Pageable#refresh()} and clears page cache. Fires {@link ListDataEvent#CONTENTS_CHANGED} which
     * causes list re render
     */
    public void refresh()
    {
        pageCache.clear();
        pageable.refresh();
        fireEvent(ListDataEvent.CONTENTS_CHANGED, -1, -1);
    }


    protected List<T> getOrLoadPage(final int pageNumber)
    {
        if(pageCache.containsKey(Integer.valueOf(pageNumber)))
        {
            return pageCache.get(Integer.valueOf(pageNumber));
        }
        else
        {
            LOG.debug("Loading page {}", Integer.valueOf(pageNumber));
            pageable.setPageNumber(pageNumber);
            final List<T> currentPage = pageable.getCurrentPage();
            pageCache.put(Integer.valueOf(pageNumber), currentPage);
            return currentPage;
        }
    }
}
