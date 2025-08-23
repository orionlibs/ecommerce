package de.hybris.platform.commerceservices.search.facetdata;

import java.io.Serializable;
import java.util.List;

public class FacetData<STATE> implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String code;
    private String name;
    private int priority;
    private boolean category;
    private boolean multiSelect;
    private boolean visible;
    private List<FacetValueData<STATE>> topValues;
    private List<FacetValueData<STATE>> values;


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


    public void setPriority(int priority)
    {
        this.priority = priority;
    }


    public int getPriority()
    {
        return this.priority;
    }


    public void setCategory(boolean category)
    {
        this.category = category;
    }


    public boolean isCategory()
    {
        return this.category;
    }


    public void setMultiSelect(boolean multiSelect)
    {
        this.multiSelect = multiSelect;
    }


    public boolean isMultiSelect()
    {
        return this.multiSelect;
    }


    public void setVisible(boolean visible)
    {
        this.visible = visible;
    }


    public boolean isVisible()
    {
        return this.visible;
    }


    public void setTopValues(List<FacetValueData<STATE>> topValues)
    {
        this.topValues = topValues;
    }


    public List<FacetValueData<STATE>> getTopValues()
    {
        return this.topValues;
    }


    public void setValues(List<FacetValueData<STATE>> values)
    {
        this.values = values;
    }


    public List<FacetValueData<STATE>> getValues()
    {
        return this.values;
    }
}
