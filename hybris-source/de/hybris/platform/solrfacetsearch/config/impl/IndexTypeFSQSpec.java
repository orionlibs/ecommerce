package de.hybris.platform.solrfacetsearch.config.impl;

import de.hybris.platform.solrfacetsearch.config.FlexibleSearchQuerySpec;
import de.hybris.platform.solrfacetsearch.config.IndexedTypeFlexibleSearchQuery;
import java.util.Map;

public class IndexTypeFSQSpec<T extends IndexedTypeFlexibleSearchQuery> implements FlexibleSearchQuerySpec
{
    private final T indexedTypeFlexileSearchQueryData;


    public IndexTypeFSQSpec(T indexedTypeFlexileSearchQueryData)
    {
        this.indexedTypeFlexileSearchQueryData = indexedTypeFlexileSearchQueryData;
    }


    public String getQuery()
    {
        return this.indexedTypeFlexileSearchQueryData.getQuery();
    }


    public Map<String, Object> createParameters()
    {
        return this.indexedTypeFlexileSearchQueryData.getParameters();
    }


    public String getUser()
    {
        return this.indexedTypeFlexileSearchQueryData.getUserId();
    }
}
