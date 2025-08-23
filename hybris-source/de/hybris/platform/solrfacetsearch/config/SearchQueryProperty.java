package de.hybris.platform.solrfacetsearch.config;

import java.io.Serializable;

public class SearchQueryProperty implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String indexedProperty;
    private boolean facet;
    private FacetType facetType;
    private Integer priority;
    private boolean includeInResponse;
    private boolean highlight;
    private String facetDisplayNameProvider;
    private String facetSortProvider;
    private String facetTopValuesProvider;
    private boolean ftsQuery;
    private Integer ftsQueryMinTermLength;
    private Float ftsQueryBoost;
    private boolean ftsFuzzyQuery;
    private Integer ftsFuzzyQueryMinTermLength;
    private Integer ftsFuzzyQueryFuzziness;
    private Float ftsFuzzyQueryBoost;
    private boolean ftsWildcardQuery;
    private Integer ftsWildcardQueryMinTermLength;
    private WildcardType ftsWildcardQueryType;
    private Float ftsWildcardQueryBoost;
    private boolean ftsPhraseQuery;
    private Float ftsPhraseQuerySlop;
    private Float ftsPhraseQueryBoost;


    public void setIndexedProperty(String indexedProperty)
    {
        this.indexedProperty = indexedProperty;
    }


    public String getIndexedProperty()
    {
        return this.indexedProperty;
    }


    public void setFacet(boolean facet)
    {
        this.facet = facet;
    }


    public boolean isFacet()
    {
        return this.facet;
    }


    public void setFacetType(FacetType facetType)
    {
        this.facetType = facetType;
    }


    public FacetType getFacetType()
    {
        return this.facetType;
    }


    public void setPriority(Integer priority)
    {
        this.priority = priority;
    }


    public Integer getPriority()
    {
        return this.priority;
    }


    public void setIncludeInResponse(boolean includeInResponse)
    {
        this.includeInResponse = includeInResponse;
    }


    public boolean isIncludeInResponse()
    {
        return this.includeInResponse;
    }


    public void setHighlight(boolean highlight)
    {
        this.highlight = highlight;
    }


    public boolean isHighlight()
    {
        return this.highlight;
    }


    public void setFacetDisplayNameProvider(String facetDisplayNameProvider)
    {
        this.facetDisplayNameProvider = facetDisplayNameProvider;
    }


    public String getFacetDisplayNameProvider()
    {
        return this.facetDisplayNameProvider;
    }


    public void setFacetSortProvider(String facetSortProvider)
    {
        this.facetSortProvider = facetSortProvider;
    }


    public String getFacetSortProvider()
    {
        return this.facetSortProvider;
    }


    public void setFacetTopValuesProvider(String facetTopValuesProvider)
    {
        this.facetTopValuesProvider = facetTopValuesProvider;
    }


    public String getFacetTopValuesProvider()
    {
        return this.facetTopValuesProvider;
    }


    public void setFtsQuery(boolean ftsQuery)
    {
        this.ftsQuery = ftsQuery;
    }


    public boolean isFtsQuery()
    {
        return this.ftsQuery;
    }


    public void setFtsQueryMinTermLength(Integer ftsQueryMinTermLength)
    {
        this.ftsQueryMinTermLength = ftsQueryMinTermLength;
    }


    public Integer getFtsQueryMinTermLength()
    {
        return this.ftsQueryMinTermLength;
    }


    public void setFtsQueryBoost(Float ftsQueryBoost)
    {
        this.ftsQueryBoost = ftsQueryBoost;
    }


    public Float getFtsQueryBoost()
    {
        return this.ftsQueryBoost;
    }


    public void setFtsFuzzyQuery(boolean ftsFuzzyQuery)
    {
        this.ftsFuzzyQuery = ftsFuzzyQuery;
    }


    public boolean isFtsFuzzyQuery()
    {
        return this.ftsFuzzyQuery;
    }


    public void setFtsFuzzyQueryMinTermLength(Integer ftsFuzzyQueryMinTermLength)
    {
        this.ftsFuzzyQueryMinTermLength = ftsFuzzyQueryMinTermLength;
    }


    public Integer getFtsFuzzyQueryMinTermLength()
    {
        return this.ftsFuzzyQueryMinTermLength;
    }


    public void setFtsFuzzyQueryFuzziness(Integer ftsFuzzyQueryFuzziness)
    {
        this.ftsFuzzyQueryFuzziness = ftsFuzzyQueryFuzziness;
    }


    public Integer getFtsFuzzyQueryFuzziness()
    {
        return this.ftsFuzzyQueryFuzziness;
    }


    public void setFtsFuzzyQueryBoost(Float ftsFuzzyQueryBoost)
    {
        this.ftsFuzzyQueryBoost = ftsFuzzyQueryBoost;
    }


    public Float getFtsFuzzyQueryBoost()
    {
        return this.ftsFuzzyQueryBoost;
    }


    public void setFtsWildcardQuery(boolean ftsWildcardQuery)
    {
        this.ftsWildcardQuery = ftsWildcardQuery;
    }


    public boolean isFtsWildcardQuery()
    {
        return this.ftsWildcardQuery;
    }


    public void setFtsWildcardQueryMinTermLength(Integer ftsWildcardQueryMinTermLength)
    {
        this.ftsWildcardQueryMinTermLength = ftsWildcardQueryMinTermLength;
    }


    public Integer getFtsWildcardQueryMinTermLength()
    {
        return this.ftsWildcardQueryMinTermLength;
    }


    public void setFtsWildcardQueryType(WildcardType ftsWildcardQueryType)
    {
        this.ftsWildcardQueryType = ftsWildcardQueryType;
    }


    public WildcardType getFtsWildcardQueryType()
    {
        return this.ftsWildcardQueryType;
    }


    public void setFtsWildcardQueryBoost(Float ftsWildcardQueryBoost)
    {
        this.ftsWildcardQueryBoost = ftsWildcardQueryBoost;
    }


    public Float getFtsWildcardQueryBoost()
    {
        return this.ftsWildcardQueryBoost;
    }


    public void setFtsPhraseQuery(boolean ftsPhraseQuery)
    {
        this.ftsPhraseQuery = ftsPhraseQuery;
    }


    public boolean isFtsPhraseQuery()
    {
        return this.ftsPhraseQuery;
    }


    public void setFtsPhraseQuerySlop(Float ftsPhraseQuerySlop)
    {
        this.ftsPhraseQuerySlop = ftsPhraseQuerySlop;
    }


    public Float getFtsPhraseQuerySlop()
    {
        return this.ftsPhraseQuerySlop;
    }


    public void setFtsPhraseQueryBoost(Float ftsPhraseQueryBoost)
    {
        this.ftsPhraseQueryBoost = ftsPhraseQueryBoost;
    }


    public Float getFtsPhraseQueryBoost()
    {
        return this.ftsPhraseQueryBoost;
    }
}
