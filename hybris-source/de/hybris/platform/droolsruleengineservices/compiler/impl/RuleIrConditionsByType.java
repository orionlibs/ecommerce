package de.hybris.platform.droolsruleengineservices.compiler.impl;

import de.hybris.platform.ruleengineservices.compiler.AbstractRuleIrBooleanCondition;
import de.hybris.platform.ruleengineservices.compiler.AbstractRuleIrPatternCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrExecutableCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrExistsCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrGroupCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrNotCondition;
import java.util.List;
import org.apache.commons.collections4.ListValuedMap;

public class RuleIrConditionsByType
{
    private ListValuedMap<Boolean, AbstractRuleIrBooleanCondition> booleanConditions;
    private ListValuedMap<String, AbstractRuleIrPatternCondition> patternConditions;
    private List<RuleIrGroupCondition> groupConditions;
    private List<RuleIrExecutableCondition> executableConditions;
    private List<RuleIrExistsCondition> existsConditions;
    private List<RuleIrNotCondition> notConditions;


    public void setBooleanConditions(ListValuedMap<Boolean, AbstractRuleIrBooleanCondition> booleanConditions)
    {
        this.booleanConditions = booleanConditions;
    }


    public ListValuedMap<Boolean, AbstractRuleIrBooleanCondition> getBooleanConditions()
    {
        return this.booleanConditions;
    }


    public void setPatternConditions(ListValuedMap<String, AbstractRuleIrPatternCondition> patternConditions)
    {
        this.patternConditions = patternConditions;
    }


    public ListValuedMap<String, AbstractRuleIrPatternCondition> getPatternConditions()
    {
        return this.patternConditions;
    }


    public void setGroupConditions(List<RuleIrGroupCondition> groupConditions)
    {
        this.groupConditions = groupConditions;
    }


    public List<RuleIrGroupCondition> getGroupConditions()
    {
        return this.groupConditions;
    }


    public void setExecutableConditions(List<RuleIrExecutableCondition> executableConditions)
    {
        this.executableConditions = executableConditions;
    }


    public List<RuleIrExecutableCondition> getExecutableConditions()
    {
        return this.executableConditions;
    }


    public void setExistsConditions(List<RuleIrExistsCondition> existsConditions)
    {
        this.existsConditions = existsConditions;
    }


    public List<RuleIrExistsCondition> getExistsConditions()
    {
        return this.existsConditions;
    }


    public void setNotConditions(List<RuleIrNotCondition> notConditions)
    {
        this.notConditions = notConditions;
    }


    public List<RuleIrNotCondition> getNotConditions()
    {
        return this.notConditions;
    }
}
