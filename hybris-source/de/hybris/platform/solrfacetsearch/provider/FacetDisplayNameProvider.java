package de.hybris.platform.solrfacetsearch.provider;

import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import org.apache.log4j.Logger;

@Deprecated(since = "5.5")
public interface FacetDisplayNameProvider
{
    public static final Logger LOG = Logger.getLogger("solrIndexThreadLogger");


    String getDisplayName(SearchQuery paramSearchQuery, String paramString);
}
