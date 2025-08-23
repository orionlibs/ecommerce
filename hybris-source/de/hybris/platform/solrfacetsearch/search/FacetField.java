package de.hybris.platform.solrfacetsearch.search;

import de.hybris.platform.solrfacetsearch.config.FacetType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FacetField implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String field;
    private Integer priority;
    private FacetType facetType;
    private String displayNameProvider;
    private String sortProvider;
    private String topValuesProvider;
    private List<String> promotedValues = new ArrayList<>();
    private List<String> excludedValues = new ArrayList<>();
    private Integer limit;
    private Integer minCount;
    private Boolean groupFacet;


    public FacetField(String field)
    {
        this.field = field;
    }


    public FacetField(String field, FacetType facetType)
    {
        this.field = field;
        this.facetType = facetType;
    }


    public String getField()
    {
        return this.field;
    }


    public void setField(String field)
    {
        this.field = field;
    }


    public Integer getPriority()
    {
        return this.priority;
    }


    public void setPriority(Integer priority)
    {
        this.priority = priority;
    }


    public FacetType getFacetType()
    {
        return this.facetType;
    }


    public void setFacetType(FacetType facetType)
    {
        this.facetType = facetType;
    }


    public String getDisplayNameProvider()
    {
        return this.displayNameProvider;
    }


    public void setDisplayNameProvider(String displayNameProvider)
    {
        this.displayNameProvider = displayNameProvider;
    }


    public String getSortProvider()
    {
        return this.sortProvider;
    }


    public void setSortProvider(String sortProvider)
    {
        this.sortProvider = sortProvider;
    }


    public String getTopValuesProvider()
    {
        return this.topValuesProvider;
    }


    public void setTopValuesProvider(String topValuesProvider)
    {
        this.topValuesProvider = topValuesProvider;
    }


    public List<String> getPromotedValues()
    {
        return this.promotedValues;
    }


    public void setPromotedValues(List<String> promotedValues)
    {
        this.promotedValues = promotedValues;
    }


    public List<String> getExcludedValues()
    {
        return this.excludedValues;
    }


    public void setExcludedValues(List<String> excludedValues)
    {
        this.excludedValues = excludedValues;
    }


    public Integer getLimit()
    {
        return this.limit;
    }


    public void setLimit(Integer limit)
    {
        this.limit = limit;
    }


    public Integer getMinCount()
    {
        return this.minCount;
    }


    public void setMinCount(Integer minCount)
    {
        this.minCount = minCount;
    }


    public Boolean getGroupFacet()
    {
        return this.groupFacet;
    }


    public void setGroupFacet(Boolean groupFacet)
    {
        this.groupFacet = groupFacet;
    }
}
