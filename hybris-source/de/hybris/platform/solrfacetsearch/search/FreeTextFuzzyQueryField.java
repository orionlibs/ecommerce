package de.hybris.platform.solrfacetsearch.search;

import java.io.Serializable;

public class FreeTextFuzzyQueryField implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String field;
    private Integer minTermLength;
    private Integer fuzziness;
    private Float boost;


    public FreeTextFuzzyQueryField(String field)
    {
        this.field = field;
    }


    public FreeTextFuzzyQueryField(String field, Integer minTermLength, Integer fuzziness, Float boost)
    {
        this.field = field;
        this.minTermLength = minTermLength;
        this.fuzziness = fuzziness;
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


    public Integer getFuzziness()
    {
        return this.fuzziness;
    }


    public void setFuzziness(Integer fuzziness)
    {
        this.fuzziness = fuzziness;
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
