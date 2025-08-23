package de.hybris.platform.cmsfacades.data;

import java.io.Serializable;

public class OptionData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String id;
    private String label;


    public void setId(String id)
    {
        this.id = id;
    }


    public String getId()
    {
        return this.id;
    }


    public void setLabel(String label)
    {
        this.label = label;
    }


    public String getLabel()
    {
        return this.label;
    }
}
