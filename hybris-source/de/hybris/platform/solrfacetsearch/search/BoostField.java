package de.hybris.platform.solrfacetsearch.search;

import java.io.Serializable;

public class BoostField implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String field;
    private SearchQuery.QueryOperator queryOperator;
    private transient Object value;
    private Float boostValue;
    private BoostType boostType;


    public BoostField(String field, SearchQuery.QueryOperator queryOperator, Object value, Float boostValue, BoostType boostType)
    {
        this.field = field;
        this.queryOperator = queryOperator;
        this.value = value;
        this.boostValue = boostValue;
        this.boostType = boostType;
    }


    public String getField()
    {
        return this.field;
    }


    public void setField(String field)
    {
        this.field = field;
    }


    public SearchQuery.QueryOperator getQueryOperator()
    {
        return this.queryOperator;
    }


    public void setQueryOperator(SearchQuery.QueryOperator queryOperator)
    {
        this.queryOperator = queryOperator;
    }


    public Object getValue()
    {
        return this.value;
    }


    public void setValue(Object value)
    {
        this.value = value;
    }


    public Float getBoostValue()
    {
        return this.boostValue;
    }


    public void setBoostValue(Float boostValue)
    {
        this.boostValue = boostValue;
    }


    public BoostType getBoostType()
    {
        return this.boostType;
    }


    public void setBoostType(BoostType boostType)
    {
        this.boostType = boostType;
    }
}
