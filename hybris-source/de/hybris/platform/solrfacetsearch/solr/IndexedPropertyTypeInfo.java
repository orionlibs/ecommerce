package de.hybris.platform.solrfacetsearch.solr;

import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import java.io.Serializable;
import java.util.Set;

public class IndexedPropertyTypeInfo implements Serializable
{
    private static final long serialVersionUID = 6778030067767357571L;
    private Class<?> javaType;
    private boolean allowFacet;
    private boolean allowGroup;
    private Set<SearchQuery.QueryOperator> supportedQueryOperators;


    public Class<?> getJavaType()
    {
        return this.javaType;
    }


    public void setJavaType(Class<?> javaType)
    {
        this.javaType = javaType;
    }


    public boolean isAllowFacet()
    {
        return this.allowFacet;
    }


    public void setAllowFacet(boolean allowFacet)
    {
        this.allowFacet = allowFacet;
    }


    public boolean isAllowGroup()
    {
        return this.allowGroup;
    }


    public void setAllowGroup(boolean allowGroup)
    {
        this.allowGroup = allowGroup;
    }


    public Set<SearchQuery.QueryOperator> getSupportedQueryOperators()
    {
        return this.supportedQueryOperators;
    }


    public void setSupportedQueryOperators(Set<SearchQuery.QueryOperator> supportedQueryOperators)
    {
        this.supportedQueryOperators = supportedQueryOperators;
    }
}
