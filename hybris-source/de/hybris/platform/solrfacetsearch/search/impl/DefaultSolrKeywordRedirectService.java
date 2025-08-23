package de.hybris.platform.solrfacetsearch.search.impl;

import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.solrfacetsearch.enums.KeywordRedirectMatchType;
import de.hybris.platform.solrfacetsearch.handler.KeywordRedirectHandler;
import de.hybris.platform.solrfacetsearch.model.redirect.SolrFacetSearchKeywordRedirectModel;
import de.hybris.platform.solrfacetsearch.search.KeywordRedirectSorter;
import de.hybris.platform.solrfacetsearch.search.KeywordRedirectValue;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.SolrFacetSearchKeywordDao;
import de.hybris.platform.solrfacetsearch.search.SolrKeywordRedirectService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultSolrKeywordRedirectService implements SolrKeywordRedirectService
{
    private SolrFacetSearchKeywordDao solrFacetSearchKeywordDao;
    private CommonI18NService commonI18NService;
    private Map<KeywordRedirectMatchType, KeywordRedirectHandler> redirectHandlers;
    private KeywordRedirectSorter keywordRedirectSorter;


    public List<KeywordRedirectValue> getKeywordRedirect(SearchQuery query)
    {
        String theQuery = query.getUserQuery();
        List<KeywordRedirectValue> result = new ArrayList<>();
        if(StringUtils.isNotBlank(theQuery))
        {
            Collection<SolrFacetSearchKeywordRedirectModel> redirects = findKeywordRedirects(query);
            for(SolrFacetSearchKeywordRedirectModel redirect : redirects)
            {
                handleKeywordMatch(result, theQuery, redirect);
            }
        }
        return result;
    }


    public SolrSearchResult attachKeywordRedirect(SolrSearchResult searchResult)
    {
        List<KeywordRedirectValue> result = getSingleKeywordRedirect(searchResult.getSearchQuery());
        if(!result.isEmpty())
        {
            searchResult.setKeywordRedirects(result);
        }
        return searchResult;
    }


    protected List<KeywordRedirectValue> getSingleKeywordRedirect(SearchQuery query)
    {
        List<KeywordRedirectValue> keywordRedirects = getKeywordRedirect(query);
        List<KeywordRedirectValue> result = new ArrayList<>();
        if(!keywordRedirects.isEmpty())
        {
            int FIRST_ELEMENT_INDEX = 0;
            result.add(keywordRedirects.get(0));
        }
        return result;
    }


    protected List<SolrFacetSearchKeywordRedirectModel> findKeywordRedirects(SearchQuery searchQuery)
    {
        String langIso = searchQuery.getLanguage();
        if(StringUtils.isBlank(langIso))
        {
            langIso = this.commonI18NService.getCurrentLanguage().getIsocode();
        }
        List<SolrFacetSearchKeywordRedirectModel> result = this.solrFacetSearchKeywordDao.findKeywords(searchQuery.getFacetSearchConfig().getName(), langIso);
        return this.keywordRedirectSorter.sort(result);
    }


    protected void handleKeywordMatch(List<KeywordRedirectValue> result, String theQuery, SolrFacetSearchKeywordRedirectModel redirect)
    {
        KeywordRedirectHandler handler = this.redirectHandlers.get(redirect.getMatchType());
        if(handler != null && handler.keywordMatches(theQuery, redirect.getKeyword(), redirect.getIgnoreCase().booleanValue()))
        {
            result.add(new KeywordRedirectValue(redirect.getKeyword(), redirect.getMatchType(), redirect.getRedirect()));
        }
    }


    @Required
    public void setSolrFacetSearchKeywordDao(SolrFacetSearchKeywordDao solrFacetSearchKeywordDao)
    {
        this.solrFacetSearchKeywordDao = solrFacetSearchKeywordDao;
    }


    @Required
    public void setCommonI18NService(CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
    }


    @Required
    public void setRedirectHandlers(Map<KeywordRedirectMatchType, KeywordRedirectHandler> redirectHandlers)
    {
        this.redirectHandlers = redirectHandlers;
    }


    @Required
    public void setKeywordRedirectSorter(KeywordRedirectSorter keywordRedirectSorter)
    {
        this.keywordRedirectSorter = keywordRedirectSorter;
    }
}
