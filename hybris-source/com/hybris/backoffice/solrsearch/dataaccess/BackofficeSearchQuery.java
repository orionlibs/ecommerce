package com.hybris.backoffice.solrsearch.dataaccess;

import com.google.common.collect.Lists;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.search.QueryField;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

public class BackofficeSearchQuery extends SearchQuery
{
    private SearchConditionData searchConditionData;


    public BackofficeSearchQuery(FacetSearchConfig facetSearchConfig, IndexedType indexedType)
    {
        super(facetSearchConfig, indexedType);
    }


    public void setSearchConditionData(SearchConditionData searchConditionData)
    {
        this.searchConditionData = searchConditionData;
    }


    public List<QueryField> getQueries()
    {
        List<SolrSearchCondition> searchConditions = this.searchConditionData.getQueryConditions();
        List<SolrSearchCondition> flatConditions = getFlatQueryConditions(searchConditions);
        List<QueryField> fieldQueries = new ArrayList<>();
        flatConditions.forEach(condition -> condition.getConditionValues().forEach(()));
        return fieldQueries;
    }


    protected List<SolrSearchCondition> getFlatQueryConditions(List<SolrSearchCondition> searchConditions)
    {
        List<SolrSearchCondition> flatConditions = new ArrayList<>();
        searchConditions.forEach(condition -> {
            if(condition.isNestedCondition())
            {
                flatConditions.addAll(getFlatQueryConditions(condition.getNestedConditions()));
            }
            else
            {
                flatConditions.add(condition);
            }
        });
        return flatConditions;
    }


    public List<SolrSearchCondition> getFilterQueryConditions()
    {
        return this.searchConditionData.getFilterQueryConditions();
    }


    public List<SolrSearchCondition> getFieldConditions(String field)
    {
        List<SolrSearchCondition> fieldConditions = Lists.newArrayList();
        populateFieldConditions(field, this.searchConditionData.getQueryConditions(), fieldConditions);
        return fieldConditions;
    }


    private void populateFieldConditions(String field, List<SolrSearchCondition> source, List<SolrSearchCondition> target)
    {
        if(CollectionUtils.isNotEmpty(source))
        {
            source.forEach(condition -> {
                if(condition.isNestedCondition())
                {
                    populateFieldConditions(field, condition.getNestedConditions(), target);
                }
                else if(StringUtils.equalsIgnoreCase(condition.getAttributeName(), field))
                {
                    target.add(condition);
                }
            });
        }
    }
}
