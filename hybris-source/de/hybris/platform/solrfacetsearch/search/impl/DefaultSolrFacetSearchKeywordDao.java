package de.hybris.platform.solrfacetsearch.search.impl;

import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.solrfacetsearch.enums.KeywordRedirectMatchType;
import de.hybris.platform.solrfacetsearch.model.redirect.SolrFacetSearchKeywordRedirectModel;
import de.hybris.platform.solrfacetsearch.search.SolrFacetSearchKeywordDao;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;

public class DefaultSolrFacetSearchKeywordDao implements SolrFacetSearchKeywordDao
{
    private FlexibleSearchService flexibleSearchService;


    public List<SolrFacetSearchKeywordRedirectModel> findKeywords(String facetSearchConfigName, String langIso)
    {
        StringBuilder query = new StringBuilder();
        query.append("SELECT {rd.").append("pk").append("}");
        query.append(" FROM {").append("SolrFacetSearchConfig").append(" as cfg ");
        query.append(" JOIN ").append("SolrFacetSearchKeywordRedirect").append(" as rd ");
        query.append(" ON {cfg.").append("pk").append("} = {rd.")
                        .append("facetSearchConfig").append("} ");
        query.append(" JOIN ").append("Language").append(" as lang ");
        query.append(" ON  {lang.").append("pk").append("} = {rd.")
                        .append("language").append("}}");
        query.append(" WHERE {cfg.").append("name").append("} = ?name ");
        query.append("   AND {lang.").append("isocode").append("} = ?iso ");
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("iso", langIso);
        queryParams.put("name", facetSearchConfigName);
        SearchResult<SolrFacetSearchKeywordRedirectModel> result = this.flexibleSearchService.search(new FlexibleSearchQuery(query.toString(), queryParams));
        return result.getResult();
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }


    public List<SolrFacetSearchKeywordRedirectModel> findKeywords(String keyword, KeywordRedirectMatchType matchType, String facetSearchConfigName, String langIso)
    {
        StringBuilder query = new StringBuilder();
        query.append("SELECT {rd.").append("pk").append("}");
        query.append("  FROM {").append("SolrFacetSearchConfig").append(" as cfg ");
        query.append("  JOIN ").append("SolrFacetSearchKeywordRedirect").append(" as rd ");
        query.append("    ON {cfg.").append("pk").append("} = {rd.")
                        .append("facetSearchConfig").append("} ");
        query.append("  JOIN ").append("Language").append(" as lang ");
        query.append("    ON {lang.").append("pk").append("} = {rd.")
                        .append("language").append("}}");
        query.append(" WHERE {cfg.").append("name").append("} = ?name ");
        query.append("   AND {lang.").append("isocode").append("} = ?iso ");
        query.append("   AND {rd.").append("keyword").append("} = ?keyword ");
        query.append("   AND {rd.").append("matchType").append("} = ?match ");
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("iso", langIso);
        queryParams.put("name", facetSearchConfigName);
        queryParams.put("match", matchType);
        queryParams.put("keyword", keyword);
        SearchResult<SolrFacetSearchKeywordRedirectModel> result = this.flexibleSearchService.search(new FlexibleSearchQuery(query.toString(), queryParams));
        return result.getResult();
    }
}
