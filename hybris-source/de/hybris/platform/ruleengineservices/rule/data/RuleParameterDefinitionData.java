package de.hybris.platform.ruleengineservices.rule.data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class RuleParameterDefinitionData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String name;
    private String description;
    private Integer priority;
    private String type;
    private Object defaultValue;
    private Boolean required;
    private List<String> validators;
    private Map<String, String> filters;
    private String defaultEditor;


    public void setName(String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return this.name;
    }


    public void setDescription(String description)
    {
        this.description = description;
    }


    public String getDescription()
    {
        return this.description;
    }


    public void setPriority(Integer priority)
    {
        this.priority = priority;
    }


    public Integer getPriority()
    {
        return this.priority;
    }


    public void setType(String type)
    {
        this.type = type;
    }


    public String getType()
    {
        return this.type;
    }


    public void setDefaultValue(Object defaultValue)
    {
        this.defaultValue = defaultValue;
    }


    public Object getDefaultValue()
    {
        return this.defaultValue;
    }


    public void setRequired(Boolean required)
    {
        this.required = required;
    }


    public Boolean getRequired()
    {
        return this.required;
    }


    public void setValidators(List<String> validators)
    {
        this.validators = validators;
    }


    public List<String> getValidators()
    {
        return this.validators;
    }


    public void setFilters(Map<String, String> filters)
    {
        this.filters = filters;
    }


    public Map<String, String> getFilters()
    {
        return this.filters;
    }


    public void setDefaultEditor(String defaultEditor)
    {
        this.defaultEditor = defaultEditor;
    }


    public String getDefaultEditor()
    {
        return this.defaultEditor;
    }
}
