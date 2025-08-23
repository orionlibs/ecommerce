package de.hybris.platform.solrfacetsearch.search;

import java.io.Serializable;

public class FreeTextPhraseQueryField implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String field;
    private Float slop;
    private Float boost;


    public FreeTextPhraseQueryField(String field)
    {
        this.field = field;
    }


    public FreeTextPhraseQueryField(String field, Float slop)
    {
        this.field = field;
        this.slop = slop;
    }


    public FreeTextPhraseQueryField(String field, Float slop, Float boost)
    {
        this.field = field;
        this.slop = slop;
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


    public Float getSlop()
    {
        return this.slop;
    }


    public void setSlop(Float slop)
    {
        this.slop = slop;
    }


    public Float getBoost()
    {
        return this.boost;
    }


    public void setBoost(Float boost)
    {
        this.boost = boost;
    }
}
