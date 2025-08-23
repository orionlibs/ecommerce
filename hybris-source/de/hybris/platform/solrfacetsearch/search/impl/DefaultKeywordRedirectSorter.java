package de.hybris.platform.solrfacetsearch.search.impl;

import de.hybris.platform.solrfacetsearch.enums.KeywordRedirectMatchType;
import de.hybris.platform.solrfacetsearch.model.redirect.SolrFacetSearchKeywordRedirectModel;
import de.hybris.platform.solrfacetsearch.search.KeywordRedirectSorter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;

public class DefaultKeywordRedirectSorter implements KeywordRedirectSorter
{
    private List<KeywordRedirectMatchType> sortOrder;
    private List<Comparator<SolrFacetSearchKeywordRedirectModel>> comparatorList;


    public List<SolrFacetSearchKeywordRedirectModel> sort(List<SolrFacetSearchKeywordRedirectModel> keywordRedirectList)
    {
        List<SolrFacetSearchKeywordRedirectModel> result = sortByComparators(keywordRedirectList);
        result = sortByMatchType(result);
        return result;
    }


    protected List<SolrFacetSearchKeywordRedirectModel> sortByMatchType(List<SolrFacetSearchKeywordRedirectModel> keywordRedirectList)
    {
        List<SolrFacetSearchKeywordRedirectModel> result = new ArrayList<>();
        List<SolrFacetSearchKeywordRedirectModel> toSort = new ArrayList<>();
        List<SolrFacetSearchKeywordRedirectModel> sorting = keywordRedirectList;
        for(KeywordRedirectMatchType machType : this.sortOrder)
        {
            for(SolrFacetSearchKeywordRedirectModel value : sorting)
            {
                if(value.getMatchType().equals(machType))
                {
                    result.add(value);
                    continue;
                }
                toSort.add(value);
            }
            sorting = toSort;
            toSort = new ArrayList<>();
        }
        result.addAll(sorting);
        return result;
    }


    @Required
    public void setSortOrder(List<KeywordRedirectMatchType> sortOrder)
    {
        this.sortOrder = sortOrder;
    }


    public void setComparatorList(List<Comparator<SolrFacetSearchKeywordRedirectModel>> comparatorList)
    {
        this.comparatorList = comparatorList;
    }


    protected List<SolrFacetSearchKeywordRedirectModel> sortByComparators(List<SolrFacetSearchKeywordRedirectModel> keywordRedirectList)
    {
        if(this.comparatorList != null && !this.comparatorList.isEmpty())
        {
            List<SolrFacetSearchKeywordRedirectModel> result = new ArrayList<>();
            result.addAll(keywordRedirectList);
            for(Comparator<SolrFacetSearchKeywordRedirectModel> comparator : this.comparatorList)
            {
                Collections.sort(result, comparator);
            }
            return result;
        }
        return keywordRedirectList;
    }
}
