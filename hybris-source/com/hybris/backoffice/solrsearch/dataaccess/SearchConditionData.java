package com.hybris.backoffice.solrsearch.dataaccess;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SearchConditionData implements Serializable
{
    private final List<SolrSearchCondition> queryConditions = new ArrayList<>();
    private final List<SolrSearchCondition> filterQueryConditions = new ArrayList<>();


    public List<SolrSearchCondition> getQueryConditions()
    {
        return this.queryConditions;
    }


    public void setSearchConditionData(List<SolrSearchCondition> queryConditions)
    {
        this.queryConditions.clear();
        this.queryConditions.addAll(queryConditions);
    }


    public void addQueryCondition(SolrSearchCondition condition)
    {
        this.queryConditions.add(condition);
    }


    public List<SolrSearchCondition> getFilterQueryConditions()
    {
        return this.filterQueryConditions;
    }


    public void setFilterQueryConditions(List<SolrSearchCondition> filterQueryConditions)
    {
        this.filterQueryConditions.clear();
        this.filterQueryConditions.addAll(filterQueryConditions);
    }


    public void addFilterQueryCondition(SolrSearchCondition condition)
    {
        this.filterQueryConditions.add(condition);
    }
}
