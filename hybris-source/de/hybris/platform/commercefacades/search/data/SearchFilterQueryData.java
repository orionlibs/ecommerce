package de.hybris.platform.commercefacades.search.data;

import de.hybris.platform.commerceservices.search.solrfacetsearch.data.FilterQueryOperator;
import java.io.Serializable;
import java.util.Set;

public class SearchFilterQueryData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String key;
    private FilterQueryOperator operator;
    private Set<String> values;


    public void setKey(String key)
    {
        this.key = key;
    }


    public String getKey()
    {
        return this.key;
    }


    public void setOperator(FilterQueryOperator operator)
    {
        this.operator = operator;
    }


    public FilterQueryOperator getOperator()
    {
        return this.operator;
    }


    public void setValues(Set<String> values)
    {
        this.values = values;
    }


    public Set<String> getValues()
    {
        return this.values;
    }
}
