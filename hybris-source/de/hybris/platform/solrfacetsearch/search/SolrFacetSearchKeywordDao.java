package de.hybris.platform.solrfacetsearch.search;

import de.hybris.platform.solrfacetsearch.enums.KeywordRedirectMatchType;
import de.hybris.platform.solrfacetsearch.model.redirect.SolrFacetSearchKeywordRedirectModel;
import java.util.List;

public interface SolrFacetSearchKeywordDao
{
    List<SolrFacetSearchKeywordRedirectModel> findKeywords(String paramString1, String paramString2);


    List<SolrFacetSearchKeywordRedirectModel> findKeywords(String paramString1, KeywordRedirectMatchType paramKeywordRedirectMatchType, String paramString2, String paramString3);
}
