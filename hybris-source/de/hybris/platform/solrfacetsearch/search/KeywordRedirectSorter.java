package de.hybris.platform.solrfacetsearch.search;

import de.hybris.platform.solrfacetsearch.model.redirect.SolrFacetSearchKeywordRedirectModel;
import java.util.List;

public interface KeywordRedirectSorter
{
    List<SolrFacetSearchKeywordRedirectModel> sort(List<SolrFacetSearchKeywordRedirectModel> paramList);
}
