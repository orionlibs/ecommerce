package de.hybris.platform.solrfacetsearch.search;

import de.hybris.platform.solrfacetsearch.search.impl.SolrSearchResult;
import java.util.List;

public interface SolrKeywordRedirectService
{
    List<KeywordRedirectValue> getKeywordRedirect(SearchQuery paramSearchQuery);


    SolrSearchResult attachKeywordRedirect(SolrSearchResult paramSolrSearchResult);
}
