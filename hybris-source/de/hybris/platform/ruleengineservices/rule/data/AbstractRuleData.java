package de.hybris.platform.ruleengineservices.rule.data;

import java.io.Serializable;
import java.util.Map;

public abstract class AbstractRuleData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String definitionId;
    private Map<String, RuleParameterData> parameters;


    public void setDefinitionId(String definitionId)
    {
        this.definitionId = definitionId;
    }


    public String getDefinitionId()
    {
        return this.definitionId;
    }


    public void setParameters(Map<String, RuleParameterData> parameters)
    {
        this.parameters = parameters;
    }


    public Map<String, RuleParameterData> getParameters()
    {
        return this.parameters;
    }
}
