package de.hybris.platform.ruleengineservices.maintenance.tasks.impl;

import com.google.common.collect.Sets;
import de.hybris.platform.ruleengine.RuleEngineActionResult;
import de.hybris.platform.ruleengine.dao.EngineRuleDao;
import de.hybris.platform.ruleengine.init.tasks.PostRulesModuleSwappingTask;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineRuleModel;
import de.hybris.platform.ruleengine.model.DroolsKIEModuleModel;
import de.hybris.platform.ruleengine.model.DroolsRuleModel;
import de.hybris.platform.ruleengine.util.RuleEngineUtils;
import de.hybris.platform.ruleengine.util.RuleMappings;
import de.hybris.platform.ruleengineservices.enums.RuleStatus;
import de.hybris.platform.ruleengineservices.model.AbstractRuleModel;
import de.hybris.platform.ruleengineservices.rule.services.RuleService;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Required;

public class UpdateRulesStatusPostRulesModuleSwappingTask implements PostRulesModuleSwappingTask
{
    private RuleService ruleService;
    private ModelService modelService;
    private EngineRuleDao engineRuleDao;


    public boolean execute(RuleEngineActionResult result)
    {
        if(!result.isActionFailed())
        {
            Collection<String> modifiedRuleCodes = result.getExecutionContext().getModifiedRuleCodes();
            markRulesAsPublished(modifiedRuleCodes);
            updateRuleStatusIfInactive(modifiedRuleCodes);
            return true;
        }
        if(Objects.nonNull(result.getExecutionContext()) && MapUtils.isNotEmpty(result.getExecutionContext().getRuleVersions()))
        {
            Set<AbstractRuleEngineRuleModel> compiledRules = getEngineRulesToRevert(result
                            .getExecutionContext().getRuleVersions(), result.getModuleName());
            getModelService().removeAll(compiledRules);
        }
        return false;
    }


    protected Set<AbstractRuleEngineRuleModel> getEngineRulesToRevert(Map<String, Long> ruleVersions, String moduleName)
    {
        return (Set<AbstractRuleEngineRuleModel>)ruleVersions.entrySet().stream().filter(e -> Objects.nonNull(e.getValue()))
                        .map(e -> getEngineRuleDao().getActiveRuleByCodeAndMaxVersion((String)e.getKey(), moduleName, ((Long)e.getValue()).longValue())).collect(Collectors.toSet());
    }


    protected void markRulesAsPublished(Collection<String> ruleCodes)
    {
        Set<AbstractRuleModel> compiledSourceRules = (Set<AbstractRuleModel>)ruleCodes.stream().map(code -> getRuleService().getRuleForCode(code)).filter(Objects::nonNull).collect(Collectors.toSet());
        updateRulesStatus(compiledSourceRules, RuleStatus.PUBLISHED);
    }


    protected void updateRulesStatus(Collection<AbstractRuleModel> rules, RuleStatus status)
    {
        rules.forEach(r -> r.setStatus(status));
        getModelService().saveAll(rules);
    }


    protected void updateRuleStatusIfInactive(Collection<String> ruleCodes)
    {
        Set<AbstractRuleModel> publishedSourceRules = (Set<AbstractRuleModel>)ruleCodes.stream().flatMap(code -> getRuleService().getAllRulesForCodeAndStatus(code, new RuleStatus[] {RuleStatus.PUBLISHED, RuleStatus.INACTIVE}).stream()).collect(Collectors.toSet());
        Set<AbstractRuleEngineRuleModel> activeEngineRules = (Set<AbstractRuleEngineRuleModel>)publishedSourceRules.stream().flatMap(s -> s.getEngineRules().stream()).filter(AbstractRuleEngineRuleModel::getActive).collect(Collectors.toSet());
        Set<AbstractRuleModel> publishedRules = Sets.newHashSet();
        for(AbstractRuleEngineRuleModel droolsRule : activeEngineRules)
        {
            DroolsKIEModuleModel rulesModule = (DroolsKIEModuleModel)RuleMappings.module((DroolsRuleModel)droolsRule);
            getModelService().refresh(rulesModule);
            if(!RuleEngineUtils.isDroolsKieModuleDeployed(rulesModule))
            {
                continue;
            }
            AbstractRuleEngineRuleModel deployedDroolsRule = getEngineRuleDao().getRuleByCodeAndMaxVersion(droolsRule.getCode(), rulesModule.getName(), rulesModule.getVersion().longValue());
            getModelService().refresh(deployedDroolsRule);
            getModelService().refresh(droolsRule);
            if(deployedDroolsRule.getActive().booleanValue() && deployedDroolsRule.getVersion().equals(droolsRule.getVersion()))
            {
                AbstractRuleModel sourceRule = deployedDroolsRule.getSourceRule();
                if(sourceRule.getStatus().equals(RuleStatus.INACTIVE))
                {
                    publishedRules.add(sourceRule);
                    continue;
                }
                if(sourceRule.getStatus().equals(RuleStatus.PUBLISHED))
                {
                    publishedSourceRules.remove(sourceRule);
                }
            }
        }
        publishedSourceRules.forEach(r -> checkIfPublishedAndUpdateStatus(publishedRules, r));
        getModelService().saveAll(publishedSourceRules);
    }


    protected void checkIfPublishedAndUpdateStatus(Set<AbstractRuleModel> publishedRules, AbstractRuleModel rule)
    {
        if(publishedRules.contains(rule))
        {
            rule.setStatus(RuleStatus.PUBLISHED);
        }
        else
        {
            rule.setStatus(RuleStatus.INACTIVE);
        }
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


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    protected EngineRuleDao getEngineRuleDao()
    {
        return this.engineRuleDao;
    }


    @Required
    public void setEngineRuleDao(EngineRuleDao engineRuleDao)
    {
        this.engineRuleDao = engineRuleDao;
    }
}
