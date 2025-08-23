package com.hybris.backoffice.solrsearch.dataaccess;

import com.google.common.collect.Lists;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;

public class SolrSearchCondition implements Serializable
{
    private final List<ConditionValue> conditionValues = Lists.newArrayList();
    private List<SolrSearchCondition> nestedConditions;
    private String attributeName;
    private String attributeType;
    private boolean multiValue;
    private Locale language;
    private final SearchQuery.Operator operator;
    private final boolean nestedQuery;
    private final boolean filterQueryCondition;


    public SolrSearchCondition(String attributeName, String attributeType, Locale language, SearchQuery.Operator operator)
    {
        this(attributeName, attributeType, false, language, operator, false);
    }


    public SolrSearchCondition(String attributeName, String attributeType, SearchQuery.Operator operator)
    {
        this(attributeName, attributeType, false, null, operator, false);
    }


    public SolrSearchCondition(String attributeName, String attributeType, boolean multiValue, Locale locale, SearchQuery.Operator operator, boolean filterQueryCondition)
    {
        this.attributeName = attributeName;
        this.attributeType = attributeType;
        this.operator = operator;
        this.multiValue = multiValue;
        this.nestedQuery = false;
        this.filterQueryCondition = filterQueryCondition;
        if(locale != null)
        {
            this.language = locale;
        }
    }


    public SolrSearchCondition(List<SolrSearchCondition> nestedConditions, SearchQuery.Operator operator)
    {
        this(nestedConditions, operator, false);
    }


    public SolrSearchCondition(List<SolrSearchCondition> nestedConditions, SearchQuery.Operator operator, boolean filterQueryCondition)
    {
        this.nestedConditions = Lists.newArrayList(nestedConditions);
        this.operator = operator;
        this.nestedQuery = true;
        this.filterQueryCondition = filterQueryCondition;
    }


    public List<SolrSearchCondition> getNestedConditions()
    {
        return this.nestedConditions;
    }


    public String getAttributeName()
    {
        return this.attributeName;
    }


    public String getAttributeType()
    {
        return this.attributeType;
    }


    public Locale getLanguage()
    {
        return this.language;
    }


    public SearchQuery.Operator getOperator()
    {
        return this.operator;
    }


    public boolean isMultiValue()
    {
        return this.multiValue;
    }


    public List<ConditionValue> getConditionValues()
    {
        return this.conditionValues;
    }


    public void addConditionValue(Object value, ValueComparisonOperator comparisonOperator)
    {
        if(!comparisonOperator.isUnary() && !(value instanceof Serializable))
        {
            throw new IllegalArgumentException("Value has to be serializable");
        }
        this.conditionValues.add(new ConditionValue((Serializable)value, comparisonOperator));
    }


    public boolean isNestedCondition()
    {
        return this.nestedQuery;
    }


    public boolean isFilterQueryCondition()
    {
        return this.filterQueryCondition;
    }
}
