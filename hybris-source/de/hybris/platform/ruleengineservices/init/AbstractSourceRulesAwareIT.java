package de.hybris.platform.ruleengineservices.init;

import com.google.common.collect.Lists;
import de.hybris.platform.ruleengine.dao.EngineRuleDao;
import de.hybris.platform.ruleengine.init.BulkyTestDataLoader;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineRuleModel;
import de.hybris.platform.ruleengineservices.model.SourceRuleModel;
import de.hybris.platform.ruleengineservices.rao.CartRAO;
import de.hybris.platform.ruleengineservices.rao.util.DefaultRaoService;
import de.hybris.platform.ruleengineservices.rule.dao.RuleDao;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractSourceRulesAwareIT extends BulkyTestDataLoader<SourceRuleModel>
{
    private static final String DEFAULT_ENCODING = "UTF-8";
    private static final String NUMBER_OF_SOURCE_RULES_TO_TEST = "ruleengineservices.test.sourcerules.amount";
    private static final String SOURCE_RULES_BASIC_IMPEX_PATH = "ruleengineservices.test.sourcerules.basic.impex.path";
    private static final String SOURCE_RULES_LOCALIZED_IMPEX_PATH = "ruleengineservices.test.sourcerules.localized.impex.path";
    private static final String RULES_DEFINITIONS_IMPEX_PATH = "ruleengineservices.test.sourcerules.ruledefinitions.impex.path";
    protected static final String TEST_SOURCE_RULE_CODE_PARAM = "ruleengineservices.test.sourcerule.code";
    protected static final String TEST_MODULE_NAME_PARAM = "ruleengineservices.test.module.name";
    protected static final String TEST_RULE_ENGINE_CONTEXT_PARAM = "ruleengineservices.test.ruleengine.context.name";
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractSourceRulesAwareIT.class);
    @Resource
    private RuleDao ruleDao;
    @Resource
    private ModelService modelService;
    @Resource
    private EngineRuleDao engineRuleDao;
    protected String testSourceRuleCode;
    protected String testKieModuleName;
    protected String testRuleEngineContextName;
    private final DefaultRaoService raoService = new DefaultRaoService();
    protected List<SourceRuleModel> sampleRules;


    @Before
    public void setUp() throws Exception
    {
        createCoreData();
        createDefaultUsers();
        createHardwareCatalog();
        this.testKieModuleName = getConfigurationService().getConfiguration().getString("ruleengineservices.test.module.name");
        this.testSourceRuleCode = getConfigurationService().getConfiguration().getString("ruleengineservices.test.sourcerule.code");
        this.testRuleEngineContextName = getConfigurationService().getConfiguration().getString("ruleengineservices.test.ruleengine.context.name");
        importCsv(getConfigurationService().getConfiguration().getString("ruleengineservices.test.sourcerules.ruledefinitions.impex.path"), "UTF-8");
        importCsv(getConfigurationService().getConfiguration().getString("ruleengineservices.test.sourcerules.basic.impex.path"), "UTF-8");
        importCsv(getConfigurationService().getConfiguration().getString("ruleengineservices.test.sourcerules.localized.impex.path"), "UTF-8");
        this.sampleRules = (List<SourceRuleModel>)this.ruleDao.findAllActiveRules().stream().map(r -> (SourceRuleModel)r).collect(Collectors.toList());
        int numberOfSourceRules = getConfigurationService().getConfiguration().getInt("ruleengineservices.test.sourcerules.amount");
        cloneSourceRules(numberOfSourceRules);
    }


    protected void cloneSourceRules(int cloneFactor)
    {
        List<SourceRuleModel> sourceRules = (List<SourceRuleModel>)getRuleDao().findAllActiveRules().stream().map(r -> (SourceRuleModel)r).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(sourceRules))
        {
            SourceRuleModel seedSourceRule = sourceRules.get(0);
            List<SourceRuleModel> rulesToSave = Lists.newArrayList();
            this.stopwatch.start();
            for(int i = 0; i < cloneFactor - sourceRules.size(); i++)
            {
                SourceRuleModel clonedRule = (SourceRuleModel)this.modelService.clone(seedSourceRule);
                clonedRule.setName(getCodeForClonedRule(seedSourceRule.getName(), i));
                clonedRule.setCode(getCodeForClonedRule(seedSourceRule.getCode(), i));
                clonedRule.setUuid(UUID.randomUUID().toString());
                rulesToSave.add(clonedRule);
            }
            LOGGER.info("Cloning source rules finished in [{}]", this.stopwatch.stop().toString());
            loadData(rulesToSave);
        }
    }


    protected void updateSourceRules(String sourceRuleCode, int... cloneIds)
    {
        for(int cloneId : cloneIds)
        {
            SourceRuleModel clonedRule = (SourceRuleModel)getRuleDao().findRuleByCode(getCodeForClonedRule(sourceRuleCode, cloneId));
            String sourceRuleActions = clonedRule.getActions();
            String newSourceRuleActions = modifyRuleActions(sourceRuleActions);
            clonedRule.setActions(newSourceRuleActions);
            getModelService().save(clonedRule);
        }
    }


    protected String modifyRuleActions(String ruleActions)
    {
        return ruleActions.replaceAll("\\{\"USD\":60\\}", "{\"USD\":70}");
    }


    protected String getCodeForClonedRule(String basicRuleCode, int cloneOrder)
    {
        return basicRuleCode + "_" + basicRuleCode;
    }


    protected void deleteRules(String ruleCode, int... cloneIds)
    {
        for(int cloneId : cloneIds)
        {
            String code = getCodeForClonedRule(ruleCode, cloneId);
            SourceRuleModel clonedRule = (SourceRuleModel)getRuleDao().findRuleByCode(code);
            getModelService().remove(clonedRule);
            AbstractRuleEngineRuleModel droolsRule = this.engineRuleDao.getRuleByCode(code, this.testKieModuleName);
            droolsRule.setActive(Boolean.valueOf(false));
            getModelService().save(droolsRule);
        }
    }


    protected List<SourceRuleModel> getSourceRulesSubset(String ruleCode, int... cloneIds)
    {
        List<SourceRuleModel> sourceRulesSubset = Lists.newArrayList();
        for(int cloneId : cloneIds)
        {
            String code = getCodeForClonedRule(ruleCode, cloneId);
            SourceRuleModel clonedRule = (SourceRuleModel)getRuleDao().findRuleByCode(code);
            if(Objects.nonNull(clonedRule))
            {
                sourceRulesSubset.add(clonedRule);
            }
        }
        return sourceRulesSubset;
    }


    protected CartRAO createCartRAO(String code, String currencyIso)
    {
        CartRAO cart = this.raoService.createCart();
        cart.setCode(code);
        cart.setCurrencyIsoCode(currencyIso);
        return cart;
    }


    protected RuleDao getRuleDao()
    {
        return this.ruleDao;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    protected EngineRuleDao getEngineRuleDao()
    {
        return this.engineRuleDao;
    }
}
