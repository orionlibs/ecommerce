package de.hybris.platform.ruleengineservices.rule.data;

import java.io.Serializable;
import java.util.Map;

public abstract class AbstractRuleDefinitionData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String id;
    private String name;
    private Integer priority;
    private String breadcrumb;
    private String translatorId;
    private Map<String, String> translatorParameters;
    private Map<String, RuleParameterDefinitionData> parameters;


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


    public void setBreadcrumb(String breadcrumb)
    {
        this.breadcrumb = breadcrumb;
    }


    public String getBreadcrumb()
    {
        return this.breadcrumb;
    }


    public void setTranslatorId(String translatorId)
    {
        this.translatorId = translatorId;
    }


    public String getTranslatorId()
    {
        return this.translatorId;
    }


    public void setTranslatorParameters(Map<String, String> translatorParameters)
    {
        this.translatorParameters = translatorParameters;
    }


    public Map<String, String> getTranslatorParameters()
    {
        return this.translatorParameters;
    }


    public void setParameters(Map<String, RuleParameterDefinitionData> parameters)
    {
        this.parameters = parameters;
    }


    public Map<String, RuleParameterDefinitionData> getParameters()
    {
        return this.parameters;
    }
}
