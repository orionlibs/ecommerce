package de.hybris.platform.ruleengine.strategies.impl;

import com.google.common.base.Preconditions;
import de.hybris.platform.ruleengine.dao.RulesModuleDao;
import de.hybris.platform.ruleengine.enums.RuleType;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineRuleModel;
import de.hybris.platform.ruleengine.model.AbstractRulesModuleModel;
import de.hybris.platform.ruleengine.model.DroolsRuleModel;
import de.hybris.platform.ruleengine.strategies.RulesModuleResolver;
import de.hybris.platform.ruleengine.util.EngineRulesRepository;
import de.hybris.platform.ruleengine.util.RuleMappings;
import de.hybris.platform.ruleengineservices.model.AbstractRuleModel;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultRulesModuleResolver implements RulesModuleResolver
{
    private RulesModuleDao rulesModuleDao;
    private EngineRulesRepository engineRulesRepository;


    public String lookupForModuleName(RuleType ruleType)
    {
        return lookupForRulesModule(ruleType).getName();
    }


    public <T extends AbstractRulesModuleModel> T lookupForRulesModule(RuleType ruleType)
    {
        Preconditions.checkArgument(Objects.nonNull(ruleType), "RuleType should be provided");
        List<AbstractRulesModuleModel> rulesModulesByRuleType = getRulesModuleDao().findActiveRulesModulesByRuleType(ruleType);
        if(CollectionUtils.isEmpty(rulesModulesByRuleType))
        {
            throw new IllegalStateException("No module found for the rule type [" + ruleType + "]");
        }
        if(rulesModulesByRuleType.size() == 1)
        {
            return (T)rulesModulesByRuleType.get(0);
        }
        if(rulesModulesByRuleType.size() > 1)
        {
            throw new IllegalStateException("More than one module found for the rule type [" + ruleType + "]");
        }
        return null;
    }


    public <T extends AbstractRulesModuleModel> List<T> lookupForRulesModules(AbstractRuleModel rule)
    {
        Preconditions.checkArgument(Objects.nonNull(rule), "Rule should be provided");
        Objects.requireNonNull(DroolsRuleModel.class);
        Objects.requireNonNull(DroolsRuleModel.class);
        return (List<T>)rule.getEngineRules().stream().filter(DroolsRuleModel.class::isInstance).map(DroolsRuleModel.class::cast)
                        .filter(r -> getEngineRulesRepository().checkEngineRuleDeployedForModule((AbstractRuleEngineRuleModel)r, RuleMappings.moduleName(r)))
                        .map(r -> RuleMappings.module(r))
                        .distinct().collect(Collectors.toList());
    }


    protected RulesModuleDao getRulesModuleDao()
    {
        return this.rulesModuleDao;
    }


    @Required
    public void setRulesModuleDao(RulesModuleDao rulesModuleDao)
    {
        this.rulesModuleDao = rulesModuleDao;
    }


    protected EngineRulesRepository getEngineRulesRepository()
    {
        return this.engineRulesRepository;
    }


    @Required
    public void setEngineRulesRepository(EngineRulesRepository engineRulesRepository)
    {
        this.engineRulesRepository = engineRulesRepository;
    }
}
