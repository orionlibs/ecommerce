package de.hybris.platform.ruleengineservices.rao;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

public class AbstractRuleActionRAO implements Serializable
{
    private String firedRuleCode;
    private String moduleName;
    private String actionStrategyKey;
    private AbstractActionedRAO appliedToObject;
    private Set<OrderEntryConsumedRAO> consumedEntries;
    private Map<String, String> metadata;


    public void setFiredRuleCode(String firedRuleCode)
    {
        this.firedRuleCode = firedRuleCode;
    }


    public String getFiredRuleCode()
    {
        return this.firedRuleCode;
    }


    public void setModuleName(String moduleName)
    {
        this.moduleName = moduleName;
    }


    public String getModuleName()
    {
        return this.moduleName;
    }


    public void setActionStrategyKey(String actionStrategyKey)
    {
        this.actionStrategyKey = actionStrategyKey;
    }


    public String getActionStrategyKey()
    {
        return this.actionStrategyKey;
    }


    public void setAppliedToObject(AbstractActionedRAO appliedToObject)
    {
        this.appliedToObject = appliedToObject;
    }


    public AbstractActionedRAO getAppliedToObject()
    {
        return this.appliedToObject;
    }


    public void setConsumedEntries(Set<OrderEntryConsumedRAO> consumedEntries)
    {
        this.consumedEntries = consumedEntries;
    }


    public Set<OrderEntryConsumedRAO> getConsumedEntries()
    {
        return this.consumedEntries;
    }


    public void setMetadata(Map<String, String> metadata)
    {
        this.metadata = metadata;
    }


    public Map<String, String> getMetadata()
    {
        return this.metadata;
    }
}
