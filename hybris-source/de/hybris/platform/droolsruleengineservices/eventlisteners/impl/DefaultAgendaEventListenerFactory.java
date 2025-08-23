package de.hybris.platform.droolsruleengineservices.eventlisteners.impl;

import com.google.common.base.Preconditions;
import de.hybris.platform.droolsruleengineservices.eventlisteners.AgendaEventListenerFactory;
import de.hybris.platform.ruleengine.impl.RuleMatchCountListener;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineContextModel;
import de.hybris.platform.ruleengine.model.DroolsRuleEngineContextModel;
import java.util.LinkedHashSet;
import java.util.Set;
import org.kie.api.event.rule.AgendaEventListener;

public class DefaultAgendaEventListenerFactory implements AgendaEventListenerFactory
{
    public Set<AgendaEventListener> createAgendaEventListeners(AbstractRuleEngineContextModel ctx)
    {
        Preconditions.checkArgument(ctx instanceof DroolsRuleEngineContextModel, "context must be of type de.hybris.platform.droolsruleengineservices.model.DroolsRuleEngineContextModel");
        RuleMatchCountListener listener = createRuleMatchCountListener((DroolsRuleEngineContextModel)ctx);
        Set<AgendaEventListener> listeners = new LinkedHashSet<>();
        if(listener != null)
        {
            listeners.add(listener);
        }
        return listeners;
    }


    protected RuleMatchCountListener createRuleMatchCountListener(DroolsRuleEngineContextModel ctx)
    {
        Long firingLimit = ctx.getRuleFiringLimit();
        if(firingLimit != null)
        {
            RuleMatchCountListener listener = new RuleMatchCountListener();
            listener.setExecutionLimit(firingLimit.longValue());
            return listener;
        }
        return null;
    }
}
