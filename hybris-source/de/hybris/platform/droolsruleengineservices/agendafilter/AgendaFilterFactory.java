package de.hybris.platform.droolsruleengineservices.agendafilter;

import de.hybris.platform.ruleengine.model.AbstractRuleEngineContextModel;
import org.kie.api.runtime.rule.AgendaFilter;

public interface AgendaFilterFactory
{
    AgendaFilter createAgendaFilter(AbstractRuleEngineContextModel paramAbstractRuleEngineContextModel);
}
