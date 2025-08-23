package de.hybris.platform.ruleengine.drools;

import de.hybris.platform.ruleengine.RuleEvaluationContext;
import org.kie.api.builder.ReleaseId;

public interface ModuleReleaseIdAware
{
    ReleaseId getDeployedKieModuleReleaseId(RuleEvaluationContext paramRuleEvaluationContext);
}
