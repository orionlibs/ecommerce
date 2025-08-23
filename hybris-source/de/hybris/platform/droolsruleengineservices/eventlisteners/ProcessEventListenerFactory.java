package de.hybris.platform.droolsruleengineservices.eventlisteners;

import de.hybris.platform.ruleengine.model.AbstractRuleEngineContextModel;
import java.util.Set;
import org.kie.api.event.process.ProcessEventListener;

public interface ProcessEventListenerFactory
{
    Set<ProcessEventListener> createProcessEventListeners(AbstractRuleEngineContextModel paramAbstractRuleEngineContextModel);
}
