package de.hybris.platform.ruleengine.test.impl;

import com.google.common.base.Preconditions;
import de.hybris.platform.ruleengine.dao.RulesModuleDao;
import de.hybris.platform.ruleengine.enums.RuleType;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineContextModel;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineRuleModel;
import de.hybris.platform.ruleengine.model.AbstractRulesModuleModel;
import de.hybris.platform.ruleengine.model.DroolsKIEBaseModel;
import de.hybris.platform.ruleengine.model.DroolsKIEModuleModel;
import de.hybris.platform.ruleengine.model.DroolsRuleEngineContextModel;
import de.hybris.platform.ruleengine.model.DroolsRuleModel;
import de.hybris.platform.ruleengine.test.RuleEngineTestSupportService;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.apache.commons.collections.CollectionUtils;
import org.fest.assertions.Assertions;
import org.springframework.beans.factory.annotation.Required;

public class DefaultRuleEngineTestSupportService implements RuleEngineTestSupportService
{
    private ModelService modelService;
    @Resource
    private RulesModuleDao rulesModuleDao;


    public AbstractRuleEngineRuleModel createRuleModel()
    {
        AbstractRuleEngineRuleModel ruleModel = (AbstractRuleEngineRuleModel)getModelService().create(DroolsRuleModel.class);
        ruleModel.setRuleType(RuleType.DEFAULT);
        return ruleModel;
    }


    public AbstractRulesModuleModel associateRulesToNewModule(String moduleName, Set<? extends AbstractRuleEngineRuleModel> rules)
    {
        DroolsKIEModuleModel rulesModule;
        try
        {
            rulesModule = (DroolsKIEModuleModel)this.rulesModuleDao.findByName(moduleName);
        }
        catch(ModelNotFoundException e)
        {
            rulesModule = (DroolsKIEModuleModel)getModelService().create(DroolsKIEModuleModel.class);
            rulesModule.setActive(Boolean.valueOf(true));
            rulesModule.setName(moduleName);
            rulesModule.setVersion(Long.valueOf(-1L));
            rulesModule.setRuleType(RuleType.DEFAULT);
            rulesModule.setMvnArtifactId(moduleName);
            rulesModule.setMvnGroupId("yunit-mvn-group");
            rulesModule.setMvnVersion("1.0");
            getModelService().save(rulesModule);
        }
        DroolsKIEBaseModel rulesBase = (DroolsKIEBaseModel)getModelService().create(DroolsKIEBaseModel.class);
        rulesBase.setName(moduleName);
        rulesBase.setKieModule(rulesModule);
        getModelService().save(rulesBase);
        rulesModule.setDefaultKIEBase(rulesBase);
        rulesBase.setKieModule(rulesModule);
        getModelService().save(rulesModule);
        rules.stream().map(r -> (DroolsRuleModel)r).forEach(r -> r.setKieBase(rulesBase));
        return (AbstractRulesModuleModel)rulesModule;
    }


    public void associateRulesModule(AbstractRulesModuleModel module, Set<? extends AbstractRuleEngineRuleModel> rules)
    {
        Preconditions.checkState(module instanceof DroolsKIEModuleModel, "module must be of type DroolsKIEModuleModel");
        DroolsKIEModuleModel droolsModule = (DroolsKIEModuleModel)module;
        DroolsKIEBaseModel baseModel = droolsModule.getDefaultKIEBase();
        if(Objects.nonNull(baseModel))
        {
            baseModel.setRules(rules);
            if(CollectionUtils.isNotEmpty(rules))
            {
                rules.stream().forEach(r -> ((DroolsRuleModel)r).setKieBase(baseModel));
            }
        }
    }


    public AbstractRulesModuleModel getTestRulesModule(AbstractRuleEngineContextModel abstractContext, Set<AbstractRuleEngineRuleModel> rules)
    {
        Preconditions.checkState(abstractContext instanceof DroolsRuleEngineContextModel, "ruleengine context must be of type DroolsRuleEngineContextModel");
        Set<DroolsRuleModel> droolsSet = (Set<DroolsRuleModel>)rules.stream().filter(r -> r instanceof DroolsRuleModel).map(r -> (DroolsRuleModel)r).collect(Collectors.toSet());
        DroolsRuleEngineContextModel context = (DroolsRuleEngineContextModel)abstractContext;
        DroolsKIEBaseModel kieBase = context.getKieSession().getKieBase();
        kieBase.setRules(droolsSet);
        getModelService().saveAll();
        return (AbstractRulesModuleModel)context.getKieSession().getKieBase().getKieModule();
    }


    public Optional<AbstractRulesModuleModel> resolveAssociatedRuleModule(AbstractRuleEngineRuleModel ruleModel)
    {
        DroolsKIEModuleModel droolsKIEModuleModel;
        AbstractRulesModuleModel ruleModule = null;
        if(ruleModel instanceof DroolsRuleModel)
        {
            DroolsRuleModel droolsRule = (DroolsRuleModel)ruleModel;
            Assertions.assertThat(droolsRule.getKieBase()).isNotNull();
            Assertions.assertThat(droolsRule.getKieBase().getKieModule()).isNotNull();
            droolsKIEModuleModel = droolsRule.getKieBase().getKieModule();
        }
        return (Optional)Optional.ofNullable(droolsKIEModuleModel);
    }


    public Consumer<AbstractRuleEngineRuleModel> decorateRuleForTest(Map<String, String> params)
    {
        return r -> setGlobals(r, params);
    }


    public String getTestModuleName(AbstractRuleEngineRuleModel ruleModel)
    {
        if(ruleModel instanceof DroolsRuleModel)
        {
            return ((DroolsRuleModel)ruleModel).getKieBase().getKieModule().getName();
        }
        return null;
    }


    protected void setGlobals(AbstractRuleEngineRuleModel ruleModel, Map<String, String> globals)
    {
        if(ruleModel instanceof DroolsRuleModel)
        {
            ((DroolsRuleModel)ruleModel).setGlobals(globals);
        }
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
}
