package de.hybris.platform.droolsruleengineservices.agendafilter.impl;

import de.hybris.platform.ruleengineservices.rule.evaluation.impl.RuleAndRuleGroupExecutionTracker;
import java.util.Objects;
import java.util.Optional;
import org.drools.core.common.InternalFactHandle;
import org.kie.api.definition.KieDefinition;
import org.kie.api.definition.rule.Rule;
import org.kie.api.runtime.rule.AgendaFilter;
import org.kie.api.runtime.rule.Match;

public class RuleAndRuleGroupTrackingAgendaFilter implements AgendaFilter
{
    public boolean accept(Match match)
    {
        Optional<RuleAndRuleGroupExecutionTracker> tracker = getTracker(match);
        if(tracker.isPresent())
        {
            Rule rule = match.getRule();
            return ((RuleAndRuleGroupExecutionTracker)tracker.get()).allowedToExecute(rule);
        }
        return true;
    }


    protected Optional<RuleAndRuleGroupExecutionTracker> getTracker(Match match)
    {
        Rule rule = match.getRule();
        if(rule.getKnowledgeType() != KieDefinition.KnowledgeType.RULE)
        {
            return Optional.empty();
        }
        Objects.requireNonNull(InternalFactHandle.class);
        Objects.requireNonNull(RuleAndRuleGroupExecutionTracker.class);
        Objects.requireNonNull(RuleAndRuleGroupExecutionTracker.class);
        return match.getFactHandles().stream().filter(InternalFactHandle.class::isInstance).map(fact -> ((InternalFactHandle)fact).getObject()).filter(RuleAndRuleGroupExecutionTracker.class::isInstance).map(RuleAndRuleGroupExecutionTracker.class::cast).findAny();
    }
}
