package de.hybris.platform.cmsfacades.data;

import java.io.Serializable;
import java.util.List;

public class DisplayConditionData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String typecode;
    private List<OptionData> options;


    public void setTypecode(String typecode)
    {
        this.typecode = typecode;
    }


    public String getTypecode()
    {
        return this.typecode;
    }


    public void setOptions(List<OptionData> options)
    {
        this.options = options;
    }


    public List<OptionData> getOptions()
    {
        return this.options;
    }
}
