package de.hybris.platform.ruleengineservices.maintenance.tasks.impl;

import de.hybris.platform.ruleengine.RuleEngineActionResult;
import de.hybris.platform.ruleengine.init.tasks.PostRulesModuleSwappingTask;
import de.hybris.platform.ruleengine.model.AbstractRulesModuleModel;
import de.hybris.platform.ruleengine.model.DroolsRuleModel;
import de.hybris.platform.ruleengine.strategies.RulesModuleResolver;
import de.hybris.platform.ruleengineservices.enums.RuleStatus;
import de.hybris.platform.ruleengineservices.model.AbstractRuleModel;
import de.hybris.platform.ruleengineservices.rule.services.RuleService;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Required;

public class UpdateRulesDeploymentsPostTask implements PostRulesModuleSwappingTask
{
    private RulesModuleResolver rulesModuleResolver;
    private ModelService modelService;
    private RuleService ruleService;


    public boolean execute(RuleEngineActionResult result)
    {
        if(!result.isActionFailed())
        {
            Collection<String> modifiedRuleCodes = result.getExecutionContext().getModifiedRuleCodes();
            Set<AbstractRuleModel> rules = getRulesForCodes(modifiedRuleCodes);
            rules.forEach(r -> r.setRulesModules(calculateDeployments(r)));
            getModelService().saveAll(rules);
        }
        return true;
    }


    protected Set<AbstractRuleModel> getRulesForCodes(Collection<String> ruleCodes)
    {
        return (Set<AbstractRuleModel>)ruleCodes.stream().map(code -> getRuleService().getAllRulesForCode(code)).flatMap(Collection::stream)
                        .filter(Objects::nonNull).collect(Collectors.toSet());
    }


    protected List<AbstractRulesModuleModel> calculateDeployments(AbstractRuleModel rule)
    {
        if(RuleStatus.PUBLISHED.equals(rule.getStatus()))
        {
            refreshRuleModules(rule);
            return getRulesModuleResolver().lookupForRulesModules(rule);
        }
        return Collections.emptyList();
    }


    protected void refreshRuleModules(AbstractRuleModel rule)
    {
        Objects.requireNonNull(DroolsRuleModel.class);
        Objects.requireNonNull(getModelService());
        rule.getEngineRules().stream().filter(DroolsRuleModel.class::isInstance).map(r -> ((DroolsRuleModel)r).getKieBase().getKieModule()).forEach(getModelService()::refresh);
    }


    protected RulesModuleResolver getRulesModuleResolver()
    {
        return this.rulesModuleResolver;
    }


    @Required
    public void setRulesModuleResolver(RulesModuleResolver rulesModuleResolver)
    {
        this.rulesModuleResolver = rulesModuleResolver;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    protected RuleService getRuleService()
    {
        return this.ruleService;
    }


    @Required
    public void setRuleService(RuleService ruleService)
    {
        this.ruleService = ruleService;
    }
}
