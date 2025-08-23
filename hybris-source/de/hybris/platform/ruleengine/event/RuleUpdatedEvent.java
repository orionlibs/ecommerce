package de.hybris.platform.ruleengine.event;

import de.hybris.platform.servicelayer.event.events.AbstractEvent;

public class RuleUpdatedEvent extends AbstractEvent
{
    private final String ruleCode;


    public RuleUpdatedEvent(String ruleCode)
    {
        this.ruleCode = ruleCode;
    }


    public String toString()
    {
        return "RuleUpdatedEvent{ruleCode='" + this.ruleCode + "'}";
    }


    public String getRuleCode()
    {
        return this.ruleCode;
    }
}
