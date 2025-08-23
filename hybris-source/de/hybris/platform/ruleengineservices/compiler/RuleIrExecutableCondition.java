package de.hybris.platform.ruleengineservices.compiler;

import java.util.Map;

public class RuleIrExecutableCondition extends AbstractRuleIrBooleanCondition
{
    private String conditionId;
    private Map<String, Object> conditionParameters;


    public void setConditionId(String conditionId)
    {
        this.conditionId = conditionId;
    }


    public String getConditionId()
    {
        return this.conditionId;
    }


    public void setConditionParameters(Map<String, Object> conditionParameters)
    {
        this.conditionParameters = conditionParameters;
    }


    public Map<String, Object> getConditionParameters()
    {
        return this.conditionParameters;
    }
}
