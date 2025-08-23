package de.hybris.platform.solrfacetsearch.search;

import java.io.Serializable;
import java.util.Objects;
import org.apache.commons.lang3.builder.EqualsBuilder;

public class RawQuery implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String field;
    private String query;
    private SearchQuery.Operator operator = SearchQuery.Operator.OR;


    public RawQuery(String query)
    {
        this.query = query;
    }


    @Deprecated(since = "5.7")
    public RawQuery(String query, SearchQuery.Operator operator)
    {
        this.query = query;
        this.operator = operator;
    }


    @Deprecated(since = "5.7")
    public RawQuery(String field, String query, SearchQuery.Operator operator)
    {
        this.field = field;
        this.query = query;
        this.operator = operator;
    }


    @Deprecated(since = "5.7")
    public String getField()
    {
        return this.field;
    }


    @Deprecated(since = "5.7")
    public void setField(String field)
    {
        this.field = field;
    }


    public String getQuery()
    {
        return this.query;
    }


    public void setQuery(String query)
    {
        this.query = query;
    }


    @Deprecated(since = "5.7")
    public SearchQuery.Operator getOperator()
    {
        return this.operator;
    }


    @Deprecated(since = "5.7")
    public void setOperator(SearchQuery.Operator operator)
    {
        this.operator = operator;
    }


    public boolean equals(Object obj)
    {
        if(this == obj)
        {
            return true;
        }
        if(obj == null || getClass() != obj.getClass())
        {
            return false;
        }
        RawQuery that = (RawQuery)obj;
        return (new EqualsBuilder())
                        .append(this.field, that.field)
                        .append(this.query, that.query)
                        .append(this.operator, that.operator)
                        .isEquals();
    }


    public int hashCode()
    {
        return Objects.hash(new Object[] {this.field, this.query, this.operator});
    }
}
