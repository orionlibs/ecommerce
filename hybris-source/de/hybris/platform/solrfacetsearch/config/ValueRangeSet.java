package de.hybris.platform.solrfacetsearch.config;

import java.io.Serializable;
import java.util.List;

public class ValueRangeSet implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String qualifier;
    private List<ValueRange> valueRanges;
    private ValueRangeType type;


    public void setQualifier(String qualifier)
    {
        this.qualifier = qualifier;
    }


    public String getQualifier()
    {
        return this.qualifier;
    }


    public void setValueRanges(List<ValueRange> valueRanges)
    {
        this.valueRanges = valueRanges;
    }


    public List<ValueRange> getValueRanges()
    {
        return this.valueRanges;
    }


    public void setType(ValueRangeType type)
    {
        this.type = type;
    }


    public ValueRangeType getType()
    {
        return this.type;
    }
}
