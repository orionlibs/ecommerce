package de.hybris.platform.ruleengine.util.impl;

import com.google.common.base.Preconditions;
import de.hybris.platform.ruleengine.dao.EngineRuleDao;
import de.hybris.platform.ruleengine.dao.RulesModuleDao;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineRuleModel;
import de.hybris.platform.ruleengine.model.AbstractRulesModuleModel;
import de.hybris.platform.ruleengine.model.DroolsKIEModuleModel;
import de.hybris.platform.ruleengine.util.EngineRulesRepository;
import de.hybris.platform.ruleengine.versioning.ModuleVersionResolver;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Required;

public class DefaultEngineRulesRepository implements EngineRulesRepository
{
    private RulesModuleDao rulesModuleDao;
    private EngineRuleDao engineRuleDao;
    private ModuleVersionResolver<DroolsKIEModuleModel> moduleVersionResolver;


    public <T extends AbstractRuleEngineRuleModel> boolean checkEngineRuleDeployedForModule(T engineRule, String moduleName)
    {
        Preconditions.checkArgument(Objects.nonNull(engineRule), "Engine rule should be provided");
        Preconditions.checkArgument(Objects.nonNull(moduleName), "Module name should be specified");
        DroolsKIEModuleModel rulesModule = (DroolsKIEModuleModel)getRulesModuleDao().findByName(moduleName);
        if(Objects.nonNull(rulesModule))
        {
            Optional<Long> deployedModuleVersion = getModuleVersionResolver().getDeployedModuleVersion((AbstractRulesModuleModel)rulesModule);
            if(deployedModuleVersion.isPresent())
            {
                AbstractRuleEngineRuleModel deployedRule = getEngineRuleDao().getRuleByCodeAndMaxVersion(engineRule.getCode(), moduleName, ((Long)deployedModuleVersion
                                .get()).longValue());
                return (Objects.nonNull(deployedRule) && deployedRule.getActive().booleanValue() && deployedRule.equals(engineRule));
            }
        }
        return false;
    }


    public <T extends AbstractRuleEngineRuleModel> Collection<T> getDeployedEngineRulesForModule(String moduleName)
    {
        Preconditions.checkArgument(Objects.nonNull(moduleName), "Module name should be specified");
        DroolsKIEModuleModel rulesModule = (DroolsKIEModuleModel)getRulesModuleDao().findByName(moduleName);
        if(Objects.nonNull(rulesModule))
        {
            Optional<Long> deployedModuleVersion = getModuleVersionResolver().getDeployedModuleVersion((AbstractRulesModuleModel)rulesModule);
            if(deployedModuleVersion.isPresent())
            {
                return getEngineRuleDao().getActiveRulesForVersion(moduleName, ((Long)deployedModuleVersion.get()).longValue());
            }
        }
        return Collections.emptyList();
    }


    public long countDeployedEngineRulesForModule(String moduleName)
    {
        return getDeployedEngineRulesForModule(moduleName).size();
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


    protected RulesModuleDao getRulesModuleDao()
    {
        return this.rulesModuleDao;
    }


    @Required
    public void setRulesModuleDao(RulesModuleDao rulesModuleDao)
    {
        this.rulesModuleDao = rulesModuleDao;
    }


    protected ModuleVersionResolver<DroolsKIEModuleModel> getModuleVersionResolver()
    {
        return this.moduleVersionResolver;
    }


    @Required
    public void setModuleVersionResolver(ModuleVersionResolver<DroolsKIEModuleModel> moduleVersionResolver)
    {
        this.moduleVersionResolver = moduleVersionResolver;
    }
}
