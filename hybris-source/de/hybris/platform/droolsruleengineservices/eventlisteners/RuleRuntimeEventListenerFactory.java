package de.hybris.platform.droolsruleengineservices.eventlisteners;

import de.hybris.platform.ruleengine.model.AbstractRuleEngineContextModel;
import java.util.Set;
import org.kie.api.event.rule.RuleRuntimeEventListener;

public interface RuleRuntimeEventListenerFactory
{
    Set<RuleRuntimeEventListener> createRuleRuntimeEventListeners(AbstractRuleEngineContextModel paramAbstractRuleEngineContextModel);
}
