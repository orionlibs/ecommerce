package de.hybris.platform.promotionengineservices.versioning;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Resources;
import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.order.CalculationService;
import de.hybris.platform.order.CartService;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.promotionengineservices.dao.PromotionDao;
import de.hybris.platform.promotionengineservices.promotionengine.impl.DefaultPromotionEngineService;
import de.hybris.platform.promotionengineservices.promotionengine.impl.PromotionEngineServiceBaseTestBase;
import de.hybris.platform.promotions.model.PromotionGroupModel;
import de.hybris.platform.promotions.model.PromotionResultModel;
import de.hybris.platform.ruleengine.MessageLevel;
import de.hybris.platform.ruleengine.RuleEngineActionResult;
import de.hybris.platform.ruleengine.RuleEngineService;
import de.hybris.platform.ruleengine.dao.RuleEngineContextDao;
import de.hybris.platform.ruleengine.enums.RuleType;
import de.hybris.platform.ruleengine.init.InitializationFuture;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineContextModel;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineRuleModel;
import de.hybris.platform.ruleengine.model.AbstractRulesModuleModel;
import de.hybris.platform.ruleengine.model.DroolsKIEBaseModel;
import de.hybris.platform.ruleengine.model.DroolsRuleModel;
import de.hybris.platform.ruleengine.test.RuleEngineTestSupportService;
import de.hybris.platform.servicelayer.i18n.daos.CurrencyDao;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.apache.commons.collections.CollectionUtils;
import org.fest.assertions.Assertions;
import org.fest.assertions.CollectionAssert;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

@IntegrationTest
public class PromotionResultVersioningIT extends PromotionEngineServiceBaseTestBase
{
    @Resource
    private UserService userService;
    @Resource
    private ModelService modelService;
    @Resource
    private RuleEngineTestSupportService ruleEngineTestSupportService;
    @Resource
    private CartService cartService;
    @Resource
    private ProductService productService;
    @Resource
    private CatalogVersionService catalogVersionService;
    @Resource
    private CalculationService calculationService;
    @Resource
    private RuleEngineContextDao ruleEngineContextDao;
    @Resource
    private RuleEngineService commerceRuleEngineService;
    @Resource
    private PromotionDao promotionDao;
    @Resource
    private DefaultPromotionEngineService promotionEngineService;
    @Resource
    private CurrencyDao currencyDao;
    private CartModel cart;
    private DroolsKIEBaseModel kieBaseModel;


    @Before
    public void setUp() throws Exception
    {
        createCoreData();
        createDefaultUsers();
        createHardwareCatalog();
        PromotionEngineServiceBaseTestBase.importCsv("/promotionengineservices/test/promotionenginesetup.impex", "UTF-8");
        PromotionEngineServiceBaseTestBase.importCsv("/promotionengineservices/test/hardwareCatalogMapping.impex", "UTF-8");
        CustomerModel customerModel = this.userService.getAnonymousUser();
        this.userService.setCurrentUser((UserModel)customerModel);
        List<CurrencyModel> currencyList = this.currencyDao.findCurrenciesByCode("USD");
        this.cart = this.cartService.getSessionCart();
        this.cart.setCurrency(currencyList.get(0));
        this.kieBaseModel = getKieBaseModel("promotions-base-junit");
    }


    @Test
    public void testPromotionResultVersion() throws Exception
    {
        String ruleName = "percentageDiscountCameraAccessories";
        AbstractRuleEngineRuleModel rule = getRuleFromResource("promotionengineservices/test/rules/percentageDiscountCameraAccessories.drl", "percentageDiscountCameraAccessories.drl", "percentageDiscountCameraAccessories");
        Assert.assertNotNull(rule);
        this.modelService.save(rule);
        Assertions.assertThat(rule.getVersion()).isEqualTo(Long.valueOf(1L));
        CartModel cart = this.cartService.getSessionCart();
        ProductModel product = this.productService.getProductForCode(this.catalogVersionService.getCatalogVersion("hwcatalog", "Online"), "HW1210-3411");
        this.cartService.addNewEntry((AbstractOrderModel)cart, product, 1L, product.getUnit());
        this.modelService.save(cart);
        this.calculationService.calculate((AbstractOrderModel)cart);
        initializeRuleEngine(new AbstractRuleEngineRuleModel[] {rule});
        PromotionGroupModel promoGroup1 = this.promotionDao.findPromotionGroupByCode("promoGroup1");
        this.promotionEngineService.updatePromotions(Collections.singleton(promoGroup1), (AbstractOrderModel)cart);
        this.modelService.detachAll();
        CartModel cartFresh = (CartModel)this.modelService.get(cart.getPk());
        Set<PromotionResultModel> allPromotionResults = cartFresh.getAllPromotionResults();
        ((CollectionAssert)Assertions.assertThat(allPromotionResults).isNotEmpty()).hasSize(1);
        PromotionResultModel promotionResult = allPromotionResults.iterator().next();
        Assertions.assertThat(promotionResult.getRuleVersion()).isEqualTo(Long.valueOf(1L));
        Assertions.assertThat(promotionResult.getModuleVersion()).isEqualTo(Long.valueOf(1L));
    }


    @Test
    public void testPromotionResultVersionIsCorrectlyAligned() throws Exception
    {
        String ruleName1 = "percentageDiscountCameraAccessories";
        AbstractRuleEngineRuleModel rule1 = getRuleFromResource("promotionengineservices/test/rules/percentageDiscountCameraAccessories.drl", "percentageDiscountCameraAccessories.drl", "percentageDiscountCameraAccessories");
        this.modelService.save(rule1);
        Assertions.assertThat(rule1.getVersion()).isEqualTo(Long.valueOf(1L));
        CartModel cart1 = this.cartService.getSessionCart();
        ProductModel product = this.productService.getProductForCode(this.catalogVersionService.getCatalogVersion("hwcatalog", "Online"), "HW1210-3411");
        this.cartService.addNewEntry((AbstractOrderModel)cart1, product, 1L, product.getUnit());
        this.modelService.save(cart1);
        this.calculationService.calculate((AbstractOrderModel)cart1);
        initializeRuleEngine(new AbstractRuleEngineRuleModel[] {rule1});
        PromotionGroupModel promoGroup1 = this.promotionDao.findPromotionGroupByCode("promoGroup1");
        this.promotionEngineService.updatePromotions(Collections.singleton(promoGroup1), (AbstractOrderModel)cart1);
        String ruleName2 = "percentageDiscountCameraAccessories2";
        AbstractRuleEngineRuleModel rule2 = getRuleFromResource("promotionengineservices/test/rules/percentageDiscountCameraAccessories2.drl", "percentageDiscountCameraAccessories.drl", "percentageDiscountCameraAccessories2");
        this.modelService.save(rule2);
        Assertions.assertThat(rule2.getVersion()).isEqualTo(Long.valueOf(2L));
        CartModel cart2 = this.cartService.getSessionCart();
        this.cartService.addNewEntry((AbstractOrderModel)cart2, product, 1L, product.getUnit());
        this.modelService.save(cart2);
        initializeRuleEngine(new AbstractRuleEngineRuleModel[] {rule2});
        this.promotionEngineService.updatePromotions(Collections.singleton(promoGroup1), (AbstractOrderModel)cart2);
        this.modelService.detachAll();
        CartModel cartFresh = (CartModel)this.modelService.get(cart2.getPk());
        Set<PromotionResultModel> allPromotionResults = cartFresh.getAllPromotionResults();
        ((CollectionAssert)Assertions.assertThat(allPromotionResults).isNotEmpty()).hasSize(1);
        PromotionResultModel promotionResult = allPromotionResults.iterator().next();
        Assertions.assertThat(promotionResult.getRuleVersion()).isEqualTo(Long.valueOf(2L));
        Assertions.assertThat(promotionResult.getModuleVersion()).isEqualTo(Long.valueOf(2L));
    }


    @Test
    public void testRuleBasedPromotionVersionIsCorrectlyAligned() throws Exception
    {
        AbstractRuleEngineRuleModel rule1 = getRuleFromResource("promotionengineservices/test/rules/percentageDiscountCameraAccessories.drl", "percentageDiscountCameraAccessories.drl", "percentageDiscountCameraAccessories");
        this.modelService.save(rule1);
        initializeRuleEngine(new AbstractRuleEngineRuleModel[] {rule1});
        Assertions.assertThat(rule1.getVersion()).isEqualTo(Long.valueOf(1L));
        Assertions.assertThat(rule1.getPromotion().getRuleVersion()).isEqualTo(Long.valueOf(1L));
        AbstractRuleEngineRuleModel rule2 = getRuleFromResource("promotionengineservices/test/rules/percentageDiscountCameraAccessories2.drl", "percentageDiscountCameraAccessories.drl", "percentageDiscountCameraAccessories2");
        this.modelService.save(rule2);
        initializeRuleEngine(new AbstractRuleEngineRuleModel[] {rule2});
        Assertions.assertThat(rule2.getVersion()).isEqualTo(Long.valueOf(2L));
        Assertions.assertThat(rule2.getPromotion().getRuleVersion()).isEqualTo(Long.valueOf(2L));
        Assertions.assertThat(rule1.getPromotion()).isNotEqualTo(rule2.getPromotion());
    }


    private String readFromResource(String resourceName) throws IOException
    {
        URL url = Resources.getResource(resourceName);
        return Resources.toString(url, Charsets.UTF_8);
    }


    protected AbstractRuleEngineRuleModel getRuleFromResource(String resourceName, String ruleCode, String ruleUUID) throws IOException
    {
        DroolsRuleModel rule = (DroolsRuleModel)createEmptyRule(ruleCode, ruleUUID);
        rule.setActive(Boolean.TRUE);
        rule.setRuleContent(readFromResource(resourceName));
        rule.setKieBase(this.kieBaseModel);
        return (AbstractRuleEngineRuleModel)rule;
    }


    protected AbstractRuleEngineRuleModel createEmptyRule(String ruleCode, String ruleUUID) throws IOException
    {
        DroolsRuleModel rule = (DroolsRuleModel)this.ruleEngineTestSupportService.createRuleModel();
        rule.setRuleType(RuleType.PROMOTION);
        rule.setCode(ruleCode);
        rule.setUuid(ruleUUID);
        rule.setActive(Boolean.FALSE);
        rule.setMaxAllowedRuns(Integer.valueOf(1));
        rule.setKieBase(this.kieBaseModel);
        this.ruleEngineTestSupportService.decorateRuleForTest((Map)new Object(this))
                        .accept(rule);
        return (AbstractRuleEngineRuleModel)rule;
    }


    protected void initializeRuleEngine(AbstractRuleEngineRuleModel... rules)
    {
        AbstractRuleEngineContextModel abstractContext = this.ruleEngineContextDao.findRuleEngineContextByName("promotions-junit-context");
        InitializationFuture future = this.commerceRuleEngineService.initialize(Lists.newArrayList((Object[])new AbstractRulesModuleModel[] {this.ruleEngineTestSupportService.getTestRulesModule(abstractContext, (Set)Arrays.<AbstractRuleEngineRuleModel>stream(rules).collect(Collectors.toSet()))}),
                        true, false);
        future.waitForInitializationToFinish();
        List<RuleEngineActionResult> results = future.getResults();
        List<RuleEngineActionResult> failedResults = (List<RuleEngineActionResult>)results.stream().filter(RuleEngineActionResult::isActionFailed).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(failedResults))
        {
            StringBuilder failedMessage = new StringBuilder();
            failedResults.forEach(r -> failedMessage.append(r.getMessagesAsString(MessageLevel.ERROR)).append(", "));
            Assert.fail("rule engine initialization failed with errors: " + failedMessage);
        }
    }
}
