package de.hybris.platform.solrfacetsearch.search;

import java.io.Serializable;

public class FreeTextQueryField implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String field;
    private Integer minTermLength;
    private Float boost;


    public FreeTextQueryField(String field)
    {
        this.field = field;
    }


    public FreeTextQueryField(String field, Integer minTermLength, Float boost)
    {
        this.field = field;
        this.minTermLength = minTermLength;
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
