package de.hybris.platform.commerceservices.search.pagedata;

import java.io.Serializable;

@Deprecated(since = "6.5", forRemoval = true)
public class SortData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String code;
    private String name;
    private boolean selected;


    public void setCode(String code)
    {
        this.code = code;
    }


    public String getCode()
    {
        return this.code;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return this.name;
    }


    public void setSelected(boolean selected)
    {
        this.selected = selected;
    }


    public boolean isSelected()
    {
        return this.selected;
    }
}
