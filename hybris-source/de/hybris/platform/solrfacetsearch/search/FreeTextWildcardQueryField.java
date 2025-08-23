package de.hybris.platform.solrfacetsearch.search;

import de.hybris.platform.solrfacetsearch.config.WildcardType;
import java.io.Serializable;

public class FreeTextWildcardQueryField implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String field;
    private Integer minTermLength;
    private WildcardType wildcardType;
    private Float boost;


    public FreeTextWildcardQueryField(String field)
    {
        this.field = field;
    }


    public FreeTextWildcardQueryField(String field, Integer minTermLength, WildcardType wildcardType, Float boost)
    {
        this.field = field;
        this.minTermLength = minTermLength;
        this.wildcardType = wildcardType;
        this.boost = boost;
    }


    public String getField()
    {
        return this.field;
    }


    public void setField(String field)
    {
        this.field = field;
    }


    public WildcardType getWildcardType()
    {
        return this.wildcardType;
    }


    public void setWildcardType(WildcardType wildcardType)
    {
        this.wildcardType = wildcardType;
    }


    public Float getBoost()
    {
        return this.boost;
    }


    public void setBoost(Float boost)
    {
        this.boost = boost;
    }


    public Integer getMinTermLength()
    {
        return this.minTermLength;
    }


    public void setMinTermLength(Integer minTermLength)
    {
        this.minTermLength = minTermLength;
    }
}
