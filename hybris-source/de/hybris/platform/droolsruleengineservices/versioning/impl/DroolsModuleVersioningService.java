package de.hybris.platform.droolsruleengineservices.versioning.impl;

import com.google.common.base.Preconditions;
import de.hybris.platform.ruleengine.RuleEngineService;
import de.hybris.platform.ruleengine.dao.EngineRuleDao;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineRuleModel;
import de.hybris.platform.ruleengine.model.AbstractRulesModuleModel;
import de.hybris.platform.ruleengine.model.DroolsKIEBaseModel;
import de.hybris.platform.ruleengine.model.DroolsKIEModuleModel;
import de.hybris.platform.ruleengine.model.DroolsRuleModel;
import de.hybris.platform.ruleengine.versioning.ModuleVersionResolver;
import de.hybris.platform.ruleengine.versioning.ModuleVersioningService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;

@Deprecated(since = "21.05", forRemoval = true)
public class DroolsModuleVersioningService implements ModuleVersioningService
{
    private static final Long DEFAULT_VERSION = Long.valueOf(0L);
    public static final String ENGINE_RULE_MODEL_NULL_MESSAGE = "AbstractRuleEngineRuleModel instance is not expected to be null here";
    private RuleEngineService ruleEngineService;
    private EngineRuleDao engineRuleDao;
    private ModelService modelService;
    private ModuleVersionResolver<DroolsKIEModuleModel> moduleVersionResolver;


    public Optional<Long> getModuleVersion(AbstractRuleEngineRuleModel ruleModel)
    {
        Preconditions.checkArgument(Objects.nonNull(ruleModel), "AbstractRuleEngineRuleModel instance is not expected to be null here");
        Optional<Long> moduleVersion = Optional.empty();
        if(ruleModel instanceof DroolsRuleModel)
        {
            DroolsRuleModel droolsRuleModel = (DroolsRuleModel)ruleModel;
            DroolsKIEBaseModel kieBase = droolsRuleModel.getKieBase();
            if(Objects.nonNull(kieBase))
            {
                DroolsKIEModuleModel moduleModel = kieBase.getKieModule();
                if(Objects.nonNull(moduleModel))
                {
                    moduleVersion = getModuleVersionResolver().getDeployedModuleVersion((AbstractRulesModuleModel)moduleModel);
                }
            }
        }
        return moduleVersion;
    }


    public void assertRuleModuleVersion(AbstractRuleEngineRuleModel engineRule, AbstractRulesModuleModel rulesModule)
    {
        Preconditions.checkArgument(Objects.nonNull(engineRule), "AbstractRuleEngineRuleModel instance is not expected to be null here");
        Preconditions.checkArgument(Objects.nonNull(rulesModule), "AbstractRulesModuleModel instance is not expected to be null here");
        Long version = engineRule.getVersion();
        if(Objects.isNull(version))
        {
            version = Long.valueOf(0L);
        }
        getModelService().refresh(rulesModule);
        if(rulesModule.getVersion().longValue() <= version.longValue())
        {
            rulesModule.setVersion(version);
            getModelService().save(rulesModule);
        }
    }


    public void assertRuleModuleVersion(AbstractRulesModuleModel ruleModule, Set<AbstractRuleEngineRuleModel> rules)
    {
        Preconditions.checkArgument(Objects.nonNull(ruleModule), "AbstractRuleEngineRuleModel instance is not expected to be null here");
        Long moduleVersion = ruleModule.getVersion();
        Long currentRulesVersion = getCurrentRulesVersion(ruleModule);
        if(Objects.nonNull(moduleVersion) && Objects.nonNull(currentRulesVersion) && currentRulesVersion.longValue() > moduleVersion.longValue())
        {
            ruleModule.setVersion(currentRulesVersion);
        }
        if(CollectionUtils.isNotEmpty(rules))
        {
            Objects.requireNonNull(DroolsRuleModel.class);
            Set<DroolsRuleModel> droolRules = (Set<DroolsRuleModel>)rules.stream().map(DroolsRuleModel.class::cast).collect(Collectors.toSet());
            droolRules.stream().max(Comparator.comparing(AbstractRuleEngineRuleModel::getVersion))
                            .ifPresent(r -> setNewVersionIfApplicable(ruleModule, currentRulesVersion, r.getVersion()));
        }
    }


    public Optional<Long> getDeployedModuleVersionForRule(String ruleCode, String moduleName)
    {
        ServicesUtil.validateParameterNotNull(ruleCode, "Rule code should be provided here");
        ServicesUtil.validateParameterNotNull(moduleName, "Module name must be provided here");
        AbstractRuleEngineRuleModel rule = getEngineRuleDao().getRuleByCode(ruleCode, moduleName);
        if(Objects.nonNull(rule) && rule instanceof DroolsRuleModel)
        {
            DroolsRuleModel droolsRule = (DroolsRuleModel)rule;
            DroolsKIEBaseModel kieBase = droolsRule.getKieBase();
            if(Objects.nonNull(kieBase))
            {
                DroolsKIEModuleModel kieModule = kieBase.getKieModule();
                if(Objects.nonNull(kieModule))
                {
                    return getModuleVersionResolver().getDeployedModuleVersion((AbstractRulesModuleModel)kieModule);
                }
            }
        }
        return Optional.empty();
    }


    protected Long getCurrentRulesVersion(AbstractRulesModuleModel rulesModule)
    {
        Long maxRuleVersion = getEngineRuleDao().getCurrentRulesSnapshotVersion(rulesModule);
        if(Objects.isNull(maxRuleVersion))
        {
            maxRuleVersion = DEFAULT_VERSION;
        }
        return maxRuleVersion;
    }


    protected void setNewVersionIfApplicable(AbstractRulesModuleModel rulesModule, Long currentRulesVersion, Long newVersion)
    {
        Long moduleVersion = null;
        if(Objects.nonNull(newVersion))
        {
            if(Objects.isNull(rulesModule.getVersion()) && Objects.nonNull(currentRulesVersion))
            {
                moduleVersion = currentRulesVersion;
            }
            if(Objects.isNull(currentRulesVersion) || newVersion.longValue() > currentRulesVersion.longValue())
            {
                moduleVersion = newVersion;
            }
            if(Objects.nonNull(moduleVersion) && !moduleVersion.equals(rulesModule.getVersion()))
            {
                rulesModule.setVersion(moduleVersion);
            }
        }
    }


    protected RuleEngineService getRuleEngineService()
    {
        return this.ruleEngineService;
    }


    public void setRuleEngineService(RuleEngineService ruleEngineService)
    {
        this.ruleEngineService = ruleEngineService;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    protected EngineRuleDao getEngineRuleDao()
    {
        return this.engineRuleDao;
    }


    public void setEngineRuleDao(EngineRuleDao engineRuleDao)
    {
        this.engineRuleDao = engineRuleDao;
    }


    protected ModuleVersionResolver<DroolsKIEModuleModel> getModuleVersionResolver()
    {
        return this.moduleVersionResolver;
    }


    public void setModuleVersionResolver(ModuleVersionResolver<DroolsKIEModuleModel> moduleVersionResolver)
    {
        this.moduleVersionResolver = moduleVersionResolver;
    }
}
