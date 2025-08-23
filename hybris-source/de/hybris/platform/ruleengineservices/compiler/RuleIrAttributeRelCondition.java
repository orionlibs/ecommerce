package de.hybris.platform.ruleengineservices.compiler;

public class RuleIrAttributeRelCondition extends AbstractRuleIrAttributeCondition
{
    private String targetVariable;
    private String targetAttribute;


    public void setTargetVariable(String targetVariable)
    {
        this.targetVariable = targetVariable;
    }


    public String getTargetVariable()
    {
        return this.targetVariable;
    }


    public void setTargetAttribute(String targetAttribute)
    {
        this.targetAttribute = targetAttribute;
    }


    public String getTargetAttribute()
    {
        return this.targetAttribute;
    }
}
