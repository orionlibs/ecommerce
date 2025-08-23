package de.hybris.platform.promotionengineservices.interceptors;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.promotionengineservices.dao.PromotionDao;
import de.hybris.platform.promotionengineservices.promotionengine.impl.PromotionEngineServiceBaseTestBase;
import de.hybris.platform.promotions.model.PromotionGroupModel;
import de.hybris.platform.ruleengine.MessageLevel;
import de.hybris.platform.ruleengine.RuleEngineActionResult;
import de.hybris.platform.ruleengine.RuleEngineService;
import de.hybris.platform.ruleengine.dao.RuleEngineContextDao;
import de.hybris.platform.ruleengine.enums.RuleType;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineContextModel;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineRuleModel;
import de.hybris.platform.ruleengine.model.AbstractRulesModuleModel;
import de.hybris.platform.ruleengine.model.DroolsKIEBaseModel;
import de.hybris.platform.ruleengine.model.DroolsRuleModel;
import de.hybris.platform.ruleengine.test.RuleEngineTestSupportService;
import de.hybris.platform.servicelayer.model.ModelService;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.fest.assertions.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

@IntegrationTest
public class RuleBasePrepareInterceptorIT extends PromotionEngineServiceBaseTestBase
{
    @Resource
    private ModelService modelService;
    @Resource
    private PromotionDao promotionDao;
    @Resource
    private RuleEngineService commerceRuleEngineService;
    @Resource
    private RuleEngineContextDao ruleEngineContextDao;
    @Resource
    private RuleEngineTestSupportService ruleEngineTestSupportService;
    private DroolsKIEBaseModel kieBaseModel;


    @Before
    public void setUp() throws ImpExException, IOException
    {
        PromotionEngineServiceBaseTestBase.importCsv("/promotionengineservices/test/promotionenginesetup.impex", "UTF-8");
        this.kieBaseModel = getKieBaseModel("promotions-base-junit");
    }


    @Test
    public void testTransferCartToOrderUpdateRule() throws IOException
    {
        DroolsRuleModel ruleModel = (DroolsRuleModel)getRuleForFile("orderPercentageDiscount.drl", "/promotionengineservices/test/rules/");
        ruleModel.setCode("orderPercentageDiscount");
        ruleModel.setVersion(Long.valueOf(0L));
        this.ruleEngineTestSupportService.decorateRuleForTest((Map)new Object(this))
                        .accept(ruleModel);
        ruleModel.setKieBase(this.kieBaseModel);
        this.modelService.save(ruleModel);
        PromotionGroupModel group = this.promotionDao.findPromotionGroupByCode("promoGroup1");
        Collection<PromotionGroupModel> groupList = new ArrayList<>();
        groupList.add(group);
        initializeRuleEngine(new AbstractRuleEngineRuleModel[] {(AbstractRuleEngineRuleModel)ruleModel});
        Assertions.assertThat(ruleModel.getVersion()).isEqualTo(0L);
        Optional<AbstractRulesModuleModel> associatedRuleModule = this.ruleEngineTestSupportService.resolveAssociatedRuleModule((AbstractRuleEngineRuleModel)ruleModel);
        Assertions.assertThat(associatedRuleModule.isPresent()).isTrue();
        Assertions.assertThat(((AbstractRulesModuleModel)associatedRuleModule.get()).getVersion()).isEqualTo(0L);
        ruleModel.setRuleContent(readRuleFile("orderPercentageDiscount1.drl", "/promotionengineservices/test/rules/"));
        this.modelService.save(ruleModel);
        initializeRuleEngine(new AbstractRuleEngineRuleModel[] {(AbstractRuleEngineRuleModel)ruleModel});
        Assertions.assertThat(ruleModel.getVersion()).isEqualTo(1L);
        associatedRuleModule = this.ruleEngineTestSupportService.resolveAssociatedRuleModule((AbstractRuleEngineRuleModel)ruleModel);
        Assertions.assertThat(associatedRuleModule.isPresent()).isTrue();
        Assertions.assertThat(((AbstractRulesModuleModel)associatedRuleModule.get()).getVersion()).isEqualTo(1L);
    }


    protected void initializeRuleEngine(AbstractRuleEngineRuleModel... rules)
    {
        AbstractRuleEngineContextModel abstractContext = this.ruleEngineContextDao.findRuleEngineContextByName("promotions-junit-context");
        List<RuleEngineActionResult> results = this.commerceRuleEngineService.initialize(Collections.singletonList(this.ruleEngineTestSupportService.getTestRulesModule(abstractContext, (Set)Arrays.<AbstractRuleEngineRuleModel>stream(rules).collect(Collectors.toSet()))), true, false)
                        .waitForInitializationToFinish().getResults();
        if(CollectionUtils.isEmpty(results))
        {
            Assert.fail("rule engine initialization failed: no results found");
        }
        RuleEngineActionResult result = results.get(0);
        if(result.isActionFailed())
        {
            Assert.fail("rule engine initialization failed with errors: " + result.getMessagesAsString(MessageLevel.ERROR));
        }
    }


    protected AbstractRuleEngineRuleModel getRuleForFile(String fileName, String path) throws IOException
    {
        DroolsRuleModel rule = (DroolsRuleModel)this.ruleEngineTestSupportService.createRuleModel();
        rule.setCode(fileName);
        rule.setUuid(fileName.substring(0, fileName.length() - 4));
        rule.setActive(Boolean.TRUE);
        rule.setRuleContent(readRuleFile(fileName, path));
        rule.setRuleType(RuleType.PROMOTION);
        rule.setKieBase(this.kieBaseModel);
        return (AbstractRuleEngineRuleModel)rule;
    }


    protected String readRuleFile(String fileName, String path) throws IOException
    {
        Path rulePath = Paths.get(getApplicationContext().getResource("classpath:" + path + fileName).getURI());
        InputStream is = Files.newInputStream(rulePath, new java.nio.file.OpenOption[0]);
        StringWriter writer = new StringWriter();
        IOUtils.copy(is, writer, Charset.forName("UTF-8"));
        return writer.toString();
    }
}
