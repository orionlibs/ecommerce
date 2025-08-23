package de.hybris.platform.servicelayer.search.paginated;

import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import java.util.Map;

public class PaginatedFlexibleSearchParameter
{
    private FlexibleSearchQuery flexibleSearchQuery;
    private SearchPageData searchPageData;
    private Map<String, String> sortCodeToQueryAlias;


    public void setFlexibleSearchQuery(FlexibleSearchQuery flexibleSearchQuery)
    {
        this.flexibleSearchQuery = flexibleSearchQuery;
    }


    public FlexibleSearchQuery getFlexibleSearchQuery()
    {
        return this.flexibleSearchQuery;
    }


    public void setSearchPageData(SearchPageData searchPageData)
    {
        this.searchPageData = searchPageData;
    }


    public SearchPageData getSearchPageData()
    {
        return this.searchPageData;
    }


    public void setSortCodeToQueryAlias(Map<String, String> sortCodeToQueryAlias)
    {
        this.sortCodeToQueryAlias = sortCodeToQueryAlias;
    }


    public Map<String, String> getSortCodeToQueryAlias()
    {
        return this.sortCodeToQueryAlias;
    }
}
