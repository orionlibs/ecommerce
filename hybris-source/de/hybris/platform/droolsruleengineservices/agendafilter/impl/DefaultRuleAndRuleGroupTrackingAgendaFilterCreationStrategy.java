package de.hybris.platform.droolsruleengineservices.agendafilter.impl;

import de.hybris.platform.droolsruleengineservices.agendafilter.AgendaFilterCreationStrategy;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineContextModel;
import org.kie.api.runtime.rule.AgendaFilter;

public class DefaultRuleAndRuleGroupTrackingAgendaFilterCreationStrategy implements AgendaFilterCreationStrategy
{
    public AgendaFilter createAgendaFilter(AbstractRuleEngineContextModel context)
    {
        return (AgendaFilter)new RuleAndRuleGroupTrackingAgendaFilter();
    }
}
