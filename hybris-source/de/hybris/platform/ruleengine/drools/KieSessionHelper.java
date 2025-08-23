package de.hybris.platform.ruleengine.drools;

import de.hybris.platform.ruleengine.RuleEvaluationContext;
import org.kie.api.runtime.KieContainer;

public interface KieSessionHelper<T> extends ModuleReleaseIdAware
{
    T initializeSession(Class<T> paramClass, RuleEvaluationContext paramRuleEvaluationContext, KieContainer paramKieContainer);


    void shutdownKieSessionPools(String paramString1, String paramString2);
}
