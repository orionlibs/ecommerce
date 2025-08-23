package de.hybris.platform.droolsruleengineservices.eventlisteners;

import de.hybris.platform.ruleengine.model.AbstractRuleEngineContextModel;
import java.util.Set;
import org.kie.api.event.rule.AgendaEventListener;

public interface AgendaEventListenerFactory
{
    Set<AgendaEventListener> createAgendaEventListeners(AbstractRuleEngineContextModel paramAbstractRuleEngineContextModel);
}
