package de.hybris.platform.ruleengine.drools.impl;

import de.hybris.platform.ruleengine.RuleEvaluationContext;
import de.hybris.platform.ruleengine.drools.ModuleReleaseIdAware;
import de.hybris.platform.ruleengine.init.RuleEngineKieModuleSwapper;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineContextModel;
import de.hybris.platform.ruleengine.model.DroolsKIEBaseModel;
import de.hybris.platform.ruleengine.model.DroolsKIEModuleModel;
import de.hybris.platform.ruleengine.model.DroolsKIESessionModel;
import de.hybris.platform.ruleengine.model.DroolsRuleEngineContextModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.Objects;
import org.drools.compiler.kproject.ReleaseIdImpl;
import org.kie.api.builder.ReleaseId;
import org.springframework.beans.factory.annotation.Required;

public class DefaultModuleReleaseIdAware implements ModuleReleaseIdAware
{
    private static final String KIE_MODULE_DUMMY_VERSION = "DUMMY_VERSION";
    private static final String KIE_MODULE_DUMMY_GROUPID = "DUMMY_GROUP";
    private static final String KIE_MODULE_DUMMY_ARTIFACTID = "DUMMY_ARTIFACT";
    private RuleEngineKieModuleSwapper ruleEngineKieModuleSwapper;


    public ReleaseId getDeployedKieModuleReleaseId(RuleEvaluationContext context)
    {
        DroolsRuleEngineContextModel ruleEngineContext = validateRuleEvaluationContext(context);
        DroolsKIESessionModel kieSession = ruleEngineContext.getKieSession();
        DroolsKIEBaseModel kieBase = kieSession.getKieBase();
        DroolsKIEModuleModel kieModule = kieBase.getKieModule();
        return getRuleEngineKieModuleSwapper()
                        .getDeployedReleaseId(kieModule, null)
                        .orElse(getDummyReleaseId(kieModule));
    }


    protected ReleaseId getDummyReleaseId(DroolsKIEModuleModel module)
    {
        String groupId = module.getMvnGroupId();
        String artifactId = module.getMvnArtifactId();
        return (ReleaseId)new ReleaseIdImpl(Objects.nonNull(groupId) ? groupId : "DUMMY_GROUP",
                        Objects.nonNull(artifactId) ? artifactId : "DUMMY_ARTIFACT", "DUMMY_VERSION");
    }


    protected DroolsRuleEngineContextModel validateRuleEvaluationContext(RuleEvaluationContext context)
    {
        ServicesUtil.validateParameterNotNull(context, "rule evaluation context must not be null");
        AbstractRuleEngineContextModel abstractREContext = context.getRuleEngineContext();
        ServicesUtil.validateParameterNotNull(abstractREContext, "rule engine context must not be null");
        if(!(abstractREContext instanceof DroolsRuleEngineContextModel))
        {
            throw new IllegalArgumentException("rule engine context " + abstractREContext.getName() + " must be of type DroolsRuleEngineContext. " + abstractREContext
                            .getItemtype() + " is not supported.");
        }
        DroolsRuleEngineContextModel ruleEngineContext = (DroolsRuleEngineContextModel)abstractREContext;
        if(Objects.nonNull(context.getFilter()) && !(context.getFilter() instanceof org.kie.api.runtime.rule.AgendaFilter))
        {
            throw new IllegalArgumentException("context.filter attribute must be of type org.kie.api.runtime.rule.AgendaFilter");
        }
        return ruleEngineContext;
    }


    protected RuleEngineKieModuleSwapper getRuleEngineKieModuleSwapper()
    {
        return this.ruleEngineKieModuleSwapper;
    }


    @Required
    public void setRuleEngineKieModuleSwapper(RuleEngineKieModuleSwapper ruleEngineKieModuleSwapper)
    {
        this.ruleEngineKieModuleSwapper = ruleEngineKieModuleSwapper;
    }
}
