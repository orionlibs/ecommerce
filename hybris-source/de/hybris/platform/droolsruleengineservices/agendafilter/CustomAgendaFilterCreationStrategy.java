package de.hybris.platform.droolsruleengineservices.agendafilter;

import de.hybris.platform.ruleengine.model.AbstractRuleEngineContextModel;
import org.kie.api.runtime.rule.AgendaFilter;

public class CustomAgendaFilterCreationStrategy implements AgendaFilterCreationStrategy
{
    public AgendaFilter createAgendaFilter(AbstractRuleEngineContextModel context)
    {
        return (AgendaFilter)new CustomAgendaFilter();
    }
}
