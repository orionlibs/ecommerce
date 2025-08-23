package de.hybris.platform.ruleengine.impl;

import com.google.common.util.concurrent.AtomicLongMap;
import de.hybris.platform.ruleengine.RuleExecutionCountListener;
import de.hybris.platform.ruleengine.exception.DroolsRuleLoopException;
import org.drools.core.event.DefaultAgendaEventListener;
import org.kie.api.definition.rule.Rule;
import org.kie.api.event.rule.AfterMatchFiredEvent;

public class RuleMatchCountListener extends DefaultAgendaEventListener implements RuleExecutionCountListener
{
    private final AtomicLongMap<Rule> map = AtomicLongMap.create();
    private long executionLimit = 0L;


    public void afterMatchFired(AfterMatchFiredEvent event)
    {
        long currentCount = this.map.addAndGet(event.getMatch().getRule(), 1L);
        if(currentCount > this.executionLimit)
        {
            throw new DroolsRuleLoopException(this.executionLimit, this.map.asMap());
        }
    }


    public void setExecutionLimit(long max)
    {
        this.executionLimit = max;
    }


    protected long getExecutionLimit()
    {
        return this.executionLimit;
    }
}
