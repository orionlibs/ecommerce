package de.hybris.platform.ruleengine.impl;

import com.google.common.base.Preconditions;
import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.ruleengine.RuleEvaluationContext;
import de.hybris.platform.ruleengine.enums.DroolsSessionType;
import de.hybris.platform.ruleengine.enums.RuleType;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineContextModel;
import de.hybris.platform.ruleengine.model.DroolsKIEBaseModel;
import de.hybris.platform.ruleengine.model.DroolsKIEModuleModel;
import de.hybris.platform.ruleengine.model.DroolsKIESessionModel;
import de.hybris.platform.ruleengine.model.DroolsRuleEngineContextModel;
import de.hybris.platform.ruleengine.model.DroolsRuleModel;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.HashSet;
import java.util.UUID;
import javax.annotation.Resource;

@IntegrationTest
public class AbstractPlatformRuleEngineServiceIT extends ServicelayerTest
{
    protected static final String INITIAL_MODULE_MVN_VERSION = "1.0.0";
    protected String ruleTemplateContent;
    protected String ruleTemplateWrongContent;
    @Resource
    protected ModelService modelService;


    private DroolsKIEModuleModel createTestModule(String moduleName)
    {
        DroolsKIEModuleModel rulesModule = (DroolsKIEModuleModel)this.modelService.create(DroolsKIEModuleModel.class);
        rulesModule.setName(moduleName);
        rulesModule.setActive(Boolean.valueOf(true));
        rulesModule.setMvnGroupId("ruleengine-test");
        rulesModule.setMvnArtifactId(moduleName);
        rulesModule.setMvnVersion("1.0.0");
        rulesModule.setRuleType(RuleType.DEFAULT);
        rulesModule.setVersion(Long.valueOf(0L));
        this.modelService.save(rulesModule);
        return rulesModule;
    }


    private DroolsKIEBaseModel createTestKieBase(String baseName, DroolsKIEModuleModel module)
    {
        DroolsKIEBaseModel kieBase = (DroolsKIEBaseModel)this.modelService.create(DroolsKIEBaseModel.class);
        kieBase.setName(baseName);
        kieBase.setKieModule(module);
        module.setDefaultKIEBase(kieBase);
        this.modelService.saveAll(new Object[] {kieBase, module});
        DroolsKIESessionModel kieSessionModel = (DroolsKIESessionModel)this.modelService.create(DroolsKIESessionModel.class);
        kieSessionModel.setName("ruleengine-test-session");
        kieSessionModel.setKieBase(kieBase);
        kieSessionModel.setSessionType(DroolsSessionType.STATELESS);
        this.modelService.save(kieSessionModel);
        this.modelService.refresh(kieBase);
        return kieBase;
    }


    protected DroolsRuleModel createNewDroolsRule(String ruleUuid, String ruleCode, String moduleName, String ruleTemplateContent, DroolsKIEBaseModel kieBase)
    {
        String ruleContent = ruleTemplateContent.replaceAll("\\$\\{rule_uuid\\}", ruleUuid).replaceAll("\\$\\{rule_code\\}", ruleCode).replaceAll("\\$\\{module_name\\}", moduleName);
        DroolsRuleModel droolsRule = (DroolsRuleModel)this.modelService.create(DroolsRuleModel.class);
        droolsRule.setUuid(ruleUuid);
        droolsRule.setCode(ruleCode);
        droolsRule.setRuleContent(ruleContent);
        droolsRule.setActive(Boolean.valueOf(true));
        droolsRule.setCurrentVersion(Boolean.valueOf(true));
        droolsRule.setRuleType(RuleType.DEFAULT);
        droolsRule.setKieBase(kieBase);
        this.modelService.save(droolsRule);
        return droolsRule;
    }


    protected DroolsKIEModuleModel createRulesForModule(String moduleName, String baseName, int numOfRules)
    {
        Preconditions.checkArgument((numOfRules > 0), "The number of rules to generate should exceed 0");
        DroolsKIEModuleModel rulesModule = createTestModule(moduleName);
        DroolsKIEBaseModel kieBase = createTestKieBase(baseName, rulesModule);
        for(int i = 0; i < numOfRules; i++)
        {
            createNewDroolsRule(UUID.randomUUID().toString(), moduleName + "_rule" + moduleName, moduleName, this.ruleTemplateContent, kieBase);
        }
        return rulesModule;
    }


    protected final RuleEvaluationContext createRuleEvaluationContext(DroolsKIEModuleModel module)
    {
        DroolsRuleEngineContextModel ruleEngineContextModel = (DroolsRuleEngineContextModel)this.modelService.create(DroolsRuleEngineContextModel.class);
        ruleEngineContextModel.setName("ruleengine-test-context");
        ruleEngineContextModel.setRuleFiringLimit(Long.valueOf(1L));
        ruleEngineContextModel.setKieSession(((DroolsKIEBaseModel)module.getKieBases().iterator().next()).getKieSessions().iterator().next());
        RuleEvaluationContext ruleEvaluationContext = new RuleEvaluationContext();
        ruleEvaluationContext.setRuleEngineContext((AbstractRuleEngineContextModel)ruleEngineContextModel);
        ruleEvaluationContext.setFilter(match -> true);
        ruleEvaluationContext.setFacts(new HashSet());
        return ruleEvaluationContext;
    }
}
