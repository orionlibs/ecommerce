package de.hybris.platform.ruleengineservices.compiler;

import java.util.Map;

public class RuleIrExecutableAction extends RuleIrAction
{
    private String actionId;
    private Map<String, Object> actionParameters;


    public void setActionId(String actionId)
    {
        this.actionId = actionId;
    }


    public String getActionId()
    {
        return this.actionId;
    }


    public void setActionParameters(Map<String, Object> actionParameters)
    {
        this.actionParameters = actionParameters;
    }


    public Map<String, Object> getActionParameters()
    {
        return this.actionParameters;
    }
}
