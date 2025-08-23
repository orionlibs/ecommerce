package de.hybris.platform.solrfacetsearch.config;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

public class SearchQueryTemplate implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String name;
    private boolean showFacets;
    private boolean restrictFieldsInResponse;
    private boolean enableHighlighting;
    private boolean group;
    private IndexedProperty groupProperty;
    private Integer groupLimit;
    private boolean groupFacets;
    private Integer pageSize;
    private String ftsQueryBuilder;
    private Map<String, String> ftsQueryBuilderParameters;
    private Map<String, SearchQueryProperty> searchQueryProperties;
    private Collection<SearchQuerySort> searchQuerySorts;


    public void setName(String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return this.name;
    }


    public void setShowFacets(boolean showFacets)
    {
        this.showFacets = showFacets;
    }


    public boolean isShowFacets()
    {
        return this.showFacets;
    }


    public void setRestrictFieldsInResponse(boolean restrictFieldsInResponse)
    {
        this.restrictFieldsInResponse = restrictFieldsInResponse;
    }


    public boolean isRestrictFieldsInResponse()
    {
        return this.restrictFieldsInResponse;
    }


    public void setEnableHighlighting(boolean enableHighlighting)
    {
        this.enableHighlighting = enableHighlighting;
    }


    public boolean isEnableHighlighting()
    {
        return this.enableHighlighting;
    }


    public void setGroup(boolean group)
    {
        this.group = group;
    }


    public boolean isGroup()
    {
        return this.group;
    }


    public void setGroupProperty(IndexedProperty groupProperty)
    {
        this.groupProperty = groupProperty;
    }


    public IndexedProperty getGroupProperty()
    {
        return this.groupProperty;
    }


    public void setGroupLimit(Integer groupLimit)
    {
        this.groupLimit = groupLimit;
    }


    public Integer getGroupLimit()
    {
        return this.groupLimit;
    }


    public void setGroupFacets(boolean groupFacets)
    {
        this.groupFacets = groupFacets;
    }


    public boolean isGroupFacets()
    {
        return this.groupFacets;
    }


    public void setPageSize(Integer pageSize)
    {
        this.pageSize = pageSize;
    }


    public Integer getPageSize()
    {
        return this.pageSize;
    }


    public void setFtsQueryBuilder(String ftsQueryBuilder)
    {
        this.ftsQueryBuilder = ftsQueryBuilder;
    }


    public String getFtsQueryBuilder()
    {
        return this.ftsQueryBuilder;
    }


    public void setFtsQueryBuilderParameters(Map<String, String> ftsQueryBuilderParameters)
    {
        this.ftsQueryBuilderParameters = ftsQueryBuilderParameters;
    }


    public Map<String, String> getFtsQueryBuilderParameters()
    {
        return this.ftsQueryBuilderParameters;
    }


    public void setSearchQueryProperties(Map<String, SearchQueryProperty> searchQueryProperties)
    {
        this.searchQueryProperties = searchQueryProperties;
    }


    public Map<String, SearchQueryProperty> getSearchQueryProperties()
    {
        return this.searchQueryProperties;
    }


    public void setSearchQuerySorts(Collection<SearchQuerySort> searchQuerySorts)
    {
        this.searchQuerySorts = searchQuerySorts;
    }


    public Collection<SearchQuerySort> getSearchQuerySorts()
    {
        return this.searchQuerySorts;
    }
}
