package de.hybris.platform.ruleengineservices.maintenance.tasks.impl;

import com.google.common.collect.Lists;
import de.hybris.platform.ruleengine.RuleEngineActionResult;
import de.hybris.platform.ruleengine.dao.EngineRuleDao;
import de.hybris.platform.ruleengine.init.tasks.PostRulesModuleSwappingTask;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineRuleModel;
import de.hybris.platform.ruleengine.model.DroolsKIEModuleModel;
import de.hybris.platform.ruleengine.versioning.ModuleVersionResolver;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Required;

public class ProvideModifiedRuleCodesPostTask implements PostRulesModuleSwappingTask
{
    private ModuleVersionResolver<DroolsKIEModuleModel> moduleVersionResolver;
    private EngineRuleDao engineRuleDao;


    public boolean execute(RuleEngineActionResult result)
    {
        if(!result.isActionFailed())
        {
            Collection<String> modifiedRuleCodes = getModifiedRuleCodes(result);
            result.getExecutionContext().setModifiedRuleCodes(modifiedRuleCodes);
            return true;
        }
        return false;
    }


    protected Collection<String> getModifiedRuleCodes(RuleEngineActionResult result)
    {
        Long deployedVersion = getModuleVersionResolver().extractModuleVersion(result.getModuleName(), result
                        .getDeployedVersion());
        Long oldVersion = getModuleVersionResolver().extractModuleVersion(result.getModuleName(), result.getOldVersion());
        List<AbstractRuleEngineRuleModel> modifiedVersionRules = Lists.newArrayList();
        if(Objects.isNull(oldVersion) && Objects.nonNull(deployedVersion))
        {
            modifiedVersionRules = getEngineRuleDao().getRulesForVersion(result.getModuleName(), deployedVersion.longValue());
        }
        if(Objects.nonNull(oldVersion) && Objects.nonNull(deployedVersion))
        {
            modifiedVersionRules = getEngineRuleDao().getRulesBetweenVersions(result.getModuleName(), oldVersion.longValue(), deployedVersion.longValue());
        }
        return (Collection<String>)modifiedVersionRules.stream().map(AbstractRuleEngineRuleModel::getCode).collect(Collectors.toList());
    }


    @Deprecated(since = "21.05", forRemoval = true)
    protected boolean rulesAreEqual(AbstractRuleEngineRuleModel rule1, AbstractRuleEngineRuleModel rule2)
    {
        boolean areEqual = false;
        if(Objects.nonNull(rule1) && Objects.nonNull(rule2) && Objects.nonNull(rule1.getVersion()))
        {
            areEqual = (rule1.getCode().equals(rule2.getCode()) && rule1.getVersion().equals(rule2.getVersion()));
        }
        return areEqual;
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
