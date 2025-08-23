/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.search.data;

import com.hybris.cockpitng.dataaccess.context.Context;
import com.hybris.cockpitng.dataaccess.context.impl.DefaultContext;
import com.hybris.cockpitng.search.data.facet.FacetData;
import java.util.Collection;

/**
 * Represents full text search data - autocorrection (spell checking result) and facets.
 */
public class FullTextSearchData
{
    private final Collection<FacetData> facets;
    private final String autocorrection;
    private final Context context;


    public FullTextSearchData(final Collection<FacetData> facets, final String autocorrection)
    {
        this(facets, autocorrection, new DefaultContext());
    }


    public FullTextSearchData(final Collection<FacetData> facets, final String autocorrection, final Context context)
    {
        this.facets = facets;
        this.autocorrection = autocorrection;
        this.context = context;
    }


    public String getAutocorrection()
    {
        return autocorrection;
    }


    public Collection<FacetData> getFacets()
    {
        return facets;
    }


    public Context getContext()
    {
        return context;
    }
}
