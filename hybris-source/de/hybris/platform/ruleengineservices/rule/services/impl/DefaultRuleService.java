package de.hybris.platform.ruleengineservices.rule.services.impl;

import com.google.common.collect.Lists;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.ruleengine.dao.EngineRuleDao;
import de.hybris.platform.ruleengine.enums.RuleType;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineRuleModel;
import de.hybris.platform.ruleengine.model.AbstractRulesModuleModel;
import de.hybris.platform.ruleengine.model.DroolsKIEModuleModel;
import de.hybris.platform.ruleengine.model.DroolsRuleEngineContextModel;
import de.hybris.platform.ruleengine.strategies.RuleEngineContextFinderStrategy;
import de.hybris.platform.ruleengine.versioning.ModuleVersionResolver;
import de.hybris.platform.ruleengineservices.enums.RuleStatus;
import de.hybris.platform.ruleengineservices.model.AbstractRuleModel;
import de.hybris.platform.ruleengineservices.model.AbstractRuleTemplateModel;
import de.hybris.platform.ruleengineservices.rule.dao.RuleDao;
import de.hybris.platform.ruleengineservices.rule.services.RuleService;
import de.hybris.platform.ruleengineservices.rule.strategies.RuleTypeMappingStrategy;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Required;

public class DefaultRuleService implements RuleService
{
    private RuleDao ruleDao;
    private EngineRuleDao engineRuleDao;
    private ModelService modelService;
    private KeyGenerator sourceRuleCodeGenerator;
    private CommonI18NService commonI18NService;
    private RuleTypeMappingStrategy ruleTypeMappingStrategy;
    private RuleEngineContextFinderStrategy ruleEngineContextFinderStrategy;
    private ModuleVersionResolver<DroolsKIEModuleModel> moduleVersionResolver;


    public <T extends AbstractRuleModel> List<T> getAllRules()
    {
        return getRuleDao().findAllRules();
    }


    public <T extends AbstractRuleModel> List<T> getAllRulesForType(Class ruleType)
    {
        return getRuleDao().findAllRulesByType(ruleType);
    }


    public <T extends AbstractRuleModel> List<T> getAllActiveRules()
    {
        return getRuleDao().findAllActiveRules();
    }


    public List<AbstractRuleModel> getAllActiveRulesForType(Class ruleType)
    {
        return getRuleDao().findAllActiveRulesByType(ruleType);
    }


    public <T extends AbstractRuleModel> List<T> getActiveRulesForCatalogVersionAndRuleType(CatalogVersionModel catalogVersion, RuleType ruleType)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("catalogVersion", catalogVersion);
        ServicesUtil.validateParameterNotNullStandardMessage("ruleType", ruleType);
        Optional<DroolsRuleEngineContextModel> ruleEngineContext = getRuleEngineContextFinderStrategy().getRuleEngineContextForCatalogVersions(Collections.singletonList(catalogVersion), ruleType);
        if(ruleEngineContext.isPresent())
        {
            ServicesUtil.validateParameterNotNull(((DroolsRuleEngineContextModel)ruleEngineContext.get()).getKieSession().getKieBase(), "rule engine context must have a kie session and kie base set");
            DroolsKIEModuleModel kieModule = ((DroolsRuleEngineContextModel)ruleEngineContext.get()).getKieSession().getKieBase().getKieModule();
            Optional<Long> deployedModuleVersion = getModuleVersionResolver().getDeployedModuleVersion((AbstractRulesModuleModel)kieModule);
            if(deployedModuleVersion.isPresent())
            {
                List<AbstractRuleEngineRuleModel> activeRules = getEngineRuleDao().getActiveRulesForVersion(kieModule.getName(), ((Long)deployedModuleVersion
                                .get()).longValue());
                return (List<T>)activeRules.stream().map(r -> r.getSourceRule()).collect(Collectors.toList());
            }
        }
        return Collections.emptyList();
    }


    public AbstractRuleModel getRuleForCode(String code)
    {
        return getRuleDao().findRuleByCode(code);
    }


    public <T extends AbstractRuleModel> List<T> getAllRulesForCode(String code)
    {
        ServicesUtil.validateParameterNotNull(code, "code must not be null");
        return getRuleDao().findAllRuleVersionsByCode(code);
    }


    public <T extends AbstractRuleModel> List<T> getAllRulesForCodeAndStatus(String code, RuleStatus... ruleStatuses)
    {
        ServicesUtil.validateParameterNotNull(code, "code must not be null");
        ServicesUtil.validateParameterNotNull(ruleStatuses, "rule status must not be null");
        return getRuleDao().findAllRuleVersionsByCodeAndStatuses(code, ruleStatuses);
    }


    public <T extends AbstractRuleModel> List<T> getAllRulesForStatus(RuleStatus... ruleStatuses)
    {
        ServicesUtil.validateParameterNotNull(ruleStatuses, "rule status must not be null");
        return getRuleDao().findAllRulesWithStatuses(ruleStatuses);
    }


    public <T extends AbstractRuleModel> T createRuleFromTemplate(AbstractRuleTemplateModel ruleTemplate)
    {
        ServicesUtil.validateParameterNotNull(ruleTemplate, "rule template must not be null");
        ServicesUtil.validateParameterNotNull(ruleTemplate.getCode(), "rule template code must not be null");
        String newRuleCode = ruleTemplate.getCode() + "-" + ruleTemplate.getCode();
        return createRuleFromTemplate(newRuleCode, ruleTemplate);
    }


    public <T extends AbstractRuleModel> T createRuleFromTemplate(String newRuleCode, AbstractRuleTemplateModel ruleTemplate)
    {
        ServicesUtil.validateParameterNotNull(newRuleCode, "rule code must not be null");
        ServicesUtil.validateParameterNotNull(ruleTemplate, "rule template must not be null");
        Class<?> ruleType = getRuleTypeFromTemplate((Class)ruleTemplate.getClass());
        AbstractRuleModel abstractRuleModel = (AbstractRuleModel)getModelService().clone(ruleTemplate, ruleType);
        abstractRuleModel.setStatus(RuleStatus.UNPUBLISHED);
        abstractRuleModel.setCode(newRuleCode);
        getModelService().save(abstractRuleModel);
        return (T)abstractRuleModel;
    }


    public AbstractRuleModel cloneRule(AbstractRuleModel source)
    {
        ServicesUtil.validateParameterNotNull(source, "rule must not be null");
        String code = (String)getSourceRuleCodeGenerator().generate();
        return cloneRule(source.getCode() + "-" + source.getCode(), source);
    }


    public AbstractRuleModel cloneRule(String newRuleCode, AbstractRuleModel source)
    {
        ServicesUtil.validateParameterNotNull(newRuleCode, "rule code must not be null");
        ServicesUtil.validateParameterNotNull(source, "rule must not be null");
        AbstractRuleModel target = (AbstractRuleModel)getModelService().clone(source);
        target.setCode(newRuleCode);
        target.setUuid(null);
        target.setStatus(RuleStatus.UNPUBLISHED);
        target.setVersion(Long.valueOf(0L));
        target.setRulesModules(Lists.newArrayList());
        getModelService().save(target);
        return target;
    }


    public Class<? extends AbstractRuleModel> getRuleTypeFromTemplate(Class<? extends AbstractRuleTemplateModel> templateType)
    {
        return getRuleTypeMappingStrategy().findRuleType(templateType);
    }


    public RuleType getEngineRuleTypeForRuleType(Class<?> type)
    {
        RuleType ruleType = getRuleDao().findEngineRuleTypeByRuleType(type);
        if(ruleType == null)
        {
            return RuleType.DEFAULT;
        }
        return ruleType;
    }


    protected RuleDao getRuleDao()
    {
        return this.ruleDao;
    }


    @Required
    public void setRuleDao(RuleDao ruleDao)
    {
        this.ruleDao = ruleDao;
    }


    public EngineRuleDao getEngineRuleDao()
    {
        return this.engineRuleDao;
    }


    @Required
    public void setEngineRuleDao(EngineRuleDao engineRuleDao)
    {
        this.engineRuleDao = engineRuleDao;
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


    protected KeyGenerator getSourceRuleCodeGenerator()
    {
        return this.sourceRuleCodeGenerator;
    }


    @Required
    public void setSourceRuleCodeGenerator(KeyGenerator sourceRuleCodeGenerator)
    {
        this.sourceRuleCodeGenerator = sourceRuleCodeGenerator;
    }


    protected CommonI18NService getCommonI18NService()
    {
        return this.commonI18NService;
    }


    @Required
    public void setCommonI18NService(CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
    }


    protected RuleTypeMappingStrategy getRuleTypeMappingStrategy()
    {
        return this.ruleTypeMappingStrategy;
    }


    @Required
    public void setRuleTypeMappingStrategy(RuleTypeMappingStrategy ruleTypeMappingStrategy)
    {
        this.ruleTypeMappingStrategy = ruleTypeMappingStrategy;
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


    protected RuleEngineContextFinderStrategy getRuleEngineContextFinderStrategy()
    {
        return this.ruleEngineContextFinderStrategy;
    }


    @Required
    public void setRuleEngineContextFinderStrategy(RuleEngineContextFinderStrategy ruleEngineContextFinderStrategy)
    {
        this.ruleEngineContextFinderStrategy = ruleEngineContextFinderStrategy;
    }
}
