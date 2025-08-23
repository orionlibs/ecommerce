/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.adminmode;

import com.hybris.cockpitng.core.config.impl.jaxb.Context;
import java.util.ArrayList;
import java.util.List;

/**
 * Stores list of filtered contexts
 */
class FilteredContext
{
    private List<Context> filteredContextList = new ArrayList<>();


    public List<Context> getFilteredContextList()
    {
        return filteredContextList;
    }


    public void setFilteredContextList(final List<Context> list)
    {
        this.filteredContextList = list;
    }
}
