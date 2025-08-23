package de.hybris.platform.commerceservices.search.facetdata;

import java.io.Serializable;
import java.util.List;

public class FacetRefinement<STATE> implements Serializable
{
    private static final long serialVersionUID = 1L;
    private List<FacetData<STATE>> facets;
    private List<BreadcrumbData<STATE>> breadcrumbs;
    private long count;


    public void setFacets(List<FacetData<STATE>> facets)
    {
        this.facets = facets;
    }


    public List<FacetData<STATE>> getFacets()
    {
        return this.facets;
    }


    public void setBreadcrumbs(List<BreadcrumbData<STATE>> breadcrumbs)
    {
        this.breadcrumbs = breadcrumbs;
    }


    public List<BreadcrumbData<STATE>> getBreadcrumbs()
    {
        return this.breadcrumbs;
    }


    public void setCount(long count)
    {
        this.count = count;
    }


    public long getCount()
    {
        return this.count;
    }
}
