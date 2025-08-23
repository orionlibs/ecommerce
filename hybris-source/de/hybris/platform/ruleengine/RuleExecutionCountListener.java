package de.hybris.platform.ruleengine;

import org.kie.api.event.rule.AgendaEventListener;

public interface RuleExecutionCountListener extends AgendaEventListener
{
    void setExecutionLimit(long paramLong);
}
