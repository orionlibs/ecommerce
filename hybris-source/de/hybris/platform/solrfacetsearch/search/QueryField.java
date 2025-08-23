package de.hybris.platform.solrfacetsearch.search;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public class QueryField implements Serializable
{
    private static final long serialVersionUID = 1L;
    public static final String ALL_FIELD = "fulltext";
    private String field;
    private Set<String> values;
    private SearchQuery.Operator operator;
    private SearchQuery.QueryOperator queryOperator;


    public QueryField(String field, String... values)
    {
        this(field, SearchQuery.Operator.AND, SearchQuery.QueryOperator.EQUAL_TO, values);
    }


    public QueryField(String field, SearchQuery.Operator operator, String... values)
    {
        this(field, operator, SearchQuery.QueryOperator.EQUAL_TO, values);
    }


    public QueryField(String field, SearchQuery.Operator operator, Set<String> values)
    {
        this(field, operator, SearchQuery.QueryOperator.EQUAL_TO, values);
    }


    public QueryField(String field, SearchQuery.Operator operator, SearchQuery.QueryOperator queryOperator, String... values)
    {
        this.field = field;
        this.operator = operator;
        this.queryOperator = queryOperator;
        this.values = new LinkedHashSet<>(Arrays.asList(values));
    }


    public QueryField(String field, SearchQuery.Operator operator, SearchQuery.QueryOperator queryOperator, Set<String> values)
    {
        this.field = field;
        this.operator = operator;
        this.queryOperator = queryOperator;
        this.values = values;
    }


    public String getField()
    {
        return this.field;
    }


    public void setField(String field)
    {
        this.field = field;
    }


    public Set<String> getValues()
    {
        return this.values;
    }


    public void setValues(Set<String> values)
    {
        this.values = new LinkedHashSet<>(values);
    }


    public SearchQuery.Operator getOperator()
    {
        return this.operator;
    }


    public void setOperator(SearchQuery.Operator operator)
    {
        this.operator = operator;
    }


    public SearchQuery.QueryOperator getQueryOperator()
    {
        return this.queryOperator;
    }


    public void setQueryOperator(SearchQuery.QueryOperator queryOperator)
    {
        this.queryOperator = queryOperator;
    }
}
