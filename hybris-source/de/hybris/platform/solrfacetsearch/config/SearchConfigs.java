package de.hybris.platform.solrfacetsearch.config;

import java.util.List;

public final class SearchConfigs
{
    public static SearchConfig createSearchConfig(List emptyList, int i)
    {
        SearchConfig config = new SearchConfig();
        config.setDefaultSortOrder(emptyList);
        config.setPageSize(i);
        return config;
    }
}
