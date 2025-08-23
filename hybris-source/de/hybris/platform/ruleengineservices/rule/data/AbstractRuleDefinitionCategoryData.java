package de.hybris.platform.ruleengineservices.rule.data;

import java.io.Serializable;

public abstract class AbstractRuleDefinitionCategoryData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String id;
    private String name;
    private Integer priority;
    private ImageData icon;


    public void setId(String id)
    {
        this.id = id;
    }


    public String getId()
    {
        return this.id;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return this.name;
    }


    public void setPriority(Integer priority)
    {
        this.priority = priority;
    }


    public Integer getPriority()
    {
        return this.priority;
    }


    public void setIcon(ImageData icon)
    {
        this.icon = icon;
    }


    public ImageData getIcon()
    {
        return this.icon;
    }
}
