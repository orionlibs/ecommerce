package de.hybris.platform.commerceservices.search.facetdata;

import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import java.util.List;

public class FacetSearchPageData<STATE, RESULT> extends SearchPageData<RESULT>
{
    private STATE currentQuery;
    private List<BreadcrumbData<STATE>> breadcrumbs;
    private List<FacetData<STATE>> facets;


    public void setCurrentQuery(STATE currentQuery)
    {
        this.currentQuery = currentQuery;
    }


    public STATE getCurrentQuery()
    {
        return this.currentQuery;
    }


    public void setBreadcrumbs(List<BreadcrumbData<STATE>> breadcrumbs)
    {
        this.breadcrumbs = breadcrumbs;
    }


    public List<BreadcrumbData<STATE>> getBreadcrumbs()
    {
        return this.breadcrumbs;
    }


    public void setFacets(List<FacetData<STATE>> facets)
    {
        this.facets = facets;
    }


    public List<FacetData<STATE>> getFacets()
    {
        return this.facets;
    }
}
