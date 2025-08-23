package de.hybris.platform.ruleengineservices.rao;

import java.util.Map;

public class DisplayMessageRAO extends AbstractRuleActionRAO
{
    private Map<String, Object> parameters;


    public void setParameters(Map<String, Object> parameters)
    {
        this.parameters = parameters;
    }


    public Map<String, Object> getParameters()
    {
        return this.parameters;
    }
}
