package de.hybris.platform.solrfacetsearch.search.impl;

import de.hybris.platform.solrfacetsearch.search.FacetSearchQueryOperatorTranslator;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;

public class DefaultFacetSearchQueryOperatorTranslator implements FacetSearchQueryOperatorTranslator
{
    private Map<SearchQuery.QueryOperator, String> queryOperatorStringMap;


    public String translate(String value, SearchQuery.QueryOperator queryOperator)
    {
        if(!this.queryOperatorStringMap.containsKey(queryOperator))
        {
            throw new IllegalArgumentException("" + queryOperator + " has no solr query template defined");
        }
        return String.format(this.queryOperatorStringMap.get(queryOperator), new Object[] {value});
    }


    public Map<SearchQuery.QueryOperator, String> getQueryOperatorStringMap()
    {
        return this.queryOperatorStringMap;
    }


    @Required
    public void setQueryOperatorStringMap(Map<SearchQuery.QueryOperator, String> queryOperatorStringMap)
    {
        this.queryOperatorStringMap = queryOperatorStringMap;
    }
}
