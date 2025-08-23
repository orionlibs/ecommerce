package de.hybris.platform.promotionengineservices.dao.impl;

import com.google.common.collect.ImmutableSet;
import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.promotionengineservices.dao.PromotionDao;
import de.hybris.platform.promotionengineservices.model.CatForPromotionSourceRuleModel;
import de.hybris.platform.promotionengineservices.model.ExcludedCatForRuleModel;
import de.hybris.platform.promotionengineservices.model.ExcludedProductForRuleModel;
import de.hybris.platform.promotionengineservices.model.ProductForPromotionSourceRuleModel;
import de.hybris.platform.promotionengineservices.model.PromotionSourceRuleModel;
import de.hybris.platform.promotionengineservices.model.RuleBasedPromotionModel;
import de.hybris.platform.promotions.model.PromotionGroupModel;
import de.hybris.platform.ruleengineservices.rule.dao.RuleDao;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.fest.assertions.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

@IntegrationTest
public class DefaultPromotionSourceRuleDaoIT extends ServicelayerTransactionalTest
{
    private static final String PRIMARY_KIE_BASE_NAME = "primary-kie-base";
    private static final String SECONDARY_KIE_BASE_NAME = "secondary-kie-base";
    @Resource
    private DefaultPromotionSourceRuleDao promotionSourceRuleDao;
    @Resource
    private RuleDao ruleDao;
    @Resource
    private PromotionDao promotionDao;
    @Resource
    private ModelService modelService;


    @Before
    public void setUp() throws ImpExException
    {
        importCsv("/promotionengineservices/test/defaultPromotionSourceRuleDaoTest.impex", "utf-8");
    }


    @Test
    public void shouldFindAllProductForPromotionSourceRuleNoProducts()
    {
        PromotionSourceRuleModel rule = (PromotionSourceRuleModel)this.ruleDao.findRuleByCode("rule1");
        List<ProductForPromotionSourceRuleModel> productsForRule = this.promotionSourceRuleDao.findAllProductForPromotionSourceRule(rule, "primary-kie-base");
        Assertions.assertThat(productsForRule).isEmpty();
    }


    @Test
    public void shouldFindAllProductForPromotionSourceRuleAndModuleNoProducts()
    {
        PromotionSourceRuleModel rule = (PromotionSourceRuleModel)this.ruleDao.findRuleByCode("rule1");
        List<ProductForPromotionSourceRuleModel> productsForRule = this.promotionSourceRuleDao.findAllProductForPromotionSourceRule(rule, "primary-kie-base");
        Assertions.assertThat(productsForRule).isEmpty();
    }


    @Test
    public void shouldFindAllProductForPromotionSourceRule()
    {
        PromotionSourceRuleModel rule = (PromotionSourceRuleModel)this.ruleDao.findRuleByCode("rule2");
        List<ProductForPromotionSourceRuleModel> productsForRule = this.promotionSourceRuleDao.findAllProductForPromotionSourceRule(rule, "primary-kie-base");
        Assertions.assertThat(productsForRule).hasSize(2);
    }


    @Test
    public void shouldFindAllProductForPromotionSourceRuleAndModule()
    {
        PromotionSourceRuleModel rule = (PromotionSourceRuleModel)this.ruleDao.findRuleByCode("rule2");
        List<ProductForPromotionSourceRuleModel> productsForRule = this.promotionSourceRuleDao.findAllProductForPromotionSourceRule(rule, "primary-kie-base");
        Assertions.assertThat(productsForRule).hasSize(2);
    }


    @Test
    public void shouldFindNoProductForPromotionSourceRuleAndModule()
    {
        PromotionSourceRuleModel rule = (PromotionSourceRuleModel)this.ruleDao.findRuleByCode("rule2");
        List<ProductForPromotionSourceRuleModel> productsForRule = this.promotionSourceRuleDao.findAllProductForPromotionSourceRule(rule, "secondary-kie-base");
        Assertions.assertThat(productsForRule).hasSize(0);
    }


    @Test
    public void shouldFindAllCatForPromotionSourceRuleNoCategories()
    {
        PromotionSourceRuleModel rule = (PromotionSourceRuleModel)this.ruleDao.findRuleByCode("rule1");
        List<CatForPromotionSourceRuleModel> categoriesForRule = this.promotionSourceRuleDao.findAllCatForPromotionSourceRule(rule, "primary-kie-base");
        Assertions.assertThat(categoriesForRule).isEmpty();
    }


    @Test
    public void shouldFindAllCatForPromotionSourceRuleForRuleAndModuleNameNoCategories()
    {
        PromotionSourceRuleModel rule = (PromotionSourceRuleModel)this.ruleDao.findRuleByCode("rule1");
        List<CatForPromotionSourceRuleModel> categoriesForRule = this.promotionSourceRuleDao.findAllCatForPromotionSourceRule(rule, "primary-kie-base");
        Assertions.assertThat(categoriesForRule).isEmpty();
    }


    @Test
    public void shouldFindAllCatForPromotionSourceRule()
    {
        PromotionSourceRuleModel rule = (PromotionSourceRuleModel)this.ruleDao.findRuleByCode("rule2");
        List<CatForPromotionSourceRuleModel> categoriesForRule = this.promotionSourceRuleDao.findAllCatForPromotionSourceRule(rule, "primary-kie-base");
        Assertions.assertThat(categoriesForRule).hasSize(2);
    }


    @Test
    public void shouldFindAllCatForPromotionSourceRuleForRuleAndModuleName()
    {
        PromotionSourceRuleModel rule = (PromotionSourceRuleModel)this.ruleDao.findRuleByCode("rule2");
        List<CatForPromotionSourceRuleModel> categoriesForRule = this.promotionSourceRuleDao.findAllCatForPromotionSourceRule(rule, "primary-kie-base");
        Assertions.assertThat(categoriesForRule).hasSize(2);
    }


    @Test
    public void shouldFindNoCatForPromotionSourceRuleForRuleAndModuleName()
    {
        PromotionSourceRuleModel rule = (PromotionSourceRuleModel)this.ruleDao.findRuleByCode("rule2");
        List<CatForPromotionSourceRuleModel> categoriesForRule = this.promotionSourceRuleDao.findAllCatForPromotionSourceRule(rule, "secondary-kie-base");
        Assertions.assertThat(categoriesForRule).hasSize(0);
    }


    @Test
    public void testFindPromotionSourceRules()
    {
        PromotionGroupModel group = this.promotionDao.findPromotionGroupByCode("website1");
        Collection<PromotionGroupModel> groups = Arrays.asList(new PromotionGroupModel[] {group});
        ImmutableSet immutableSet = ImmutableSet.of("576", "brand_5");
        List<RuleBasedPromotionModel> promotions = this.promotionSourceRuleDao.findPromotions(groups, "111111", (Set)immutableSet);
        Assert.assertNotNull(promotions);
        Assert.assertEquals(1L, promotions.size());
        Set<String> codes = (Set<String>)promotions.stream().map(p -> p.getCode()).collect(Collectors.toSet());
        Assert.assertTrue(codes.contains("promotion2"));
    }


    @Test
    public void testFindPromotionSourceRulesWithinDateRange()
    {
        PromotionGroupModel group = this.promotionDao.findPromotionGroupByCode("website1");
        Collection<PromotionGroupModel> groups = Collections.singletonList(group);
        List<RuleBasedPromotionModel> publishedPromotions = this.promotionSourceRuleDao.findPromotions(groups, "13",
                        Collections.emptySet());
        Assert.assertNotNull(publishedPromotions);
        Assert.assertEquals(1L, publishedPromotions.size());
        Assert.assertTrue(((Set)publishedPromotions.stream().map(p -> p.getCode()).collect(Collectors.toSet())).contains("promotion13"));
    }


    @Test
    public void testFindPromotionSourceRulesOutOfDateRange()
    {
        PromotionGroupModel group = this.promotionDao.findPromotionGroupByCode("website1");
        Collection<PromotionGroupModel> groups = Collections.singletonList(group);
        List<RuleBasedPromotionModel> outdatedPublishedPromotions = this.promotionSourceRuleDao.findPromotions(groups, "14",
                        Collections.emptySet());
        Assert.assertNotNull(outdatedPublishedPromotions);
        Assert.assertEquals(0L, outdatedPublishedPromotions.size());
        List<RuleBasedPromotionModel> outdatedModifiedPromotions = this.promotionSourceRuleDao.findPromotions(groups, "17",
                        Collections.emptySet());
        Assert.assertNotNull(outdatedModifiedPromotions);
        Assert.assertEquals(0L, outdatedModifiedPromotions.size());
        List<RuleBasedPromotionModel> outdatedPublishedPromotionsForCategories = this.promotionSourceRuleDao.findPromotions(groups, "14",
                        (Set)ImmutableSet.of("581"));
        Assert.assertNotNull(outdatedPublishedPromotionsForCategories);
        Assert.assertEquals(0L, outdatedPublishedPromotionsForCategories.size());
        List<RuleBasedPromotionModel> outdatedModifiedPromotionsForCategories = this.promotionSourceRuleDao.findPromotions(groups, "17",
                        (Set)ImmutableSet.of("584"));
        Assert.assertNotNull(outdatedModifiedPromotionsForCategories);
        Assert.assertEquals(0L, outdatedModifiedPromotionsForCategories.size());
    }


    @Test
    public void testFindUnpublishedPromotionSourceRules()
    {
        PromotionGroupModel group = this.promotionDao.findPromotionGroupByCode("website1");
        Collection<PromotionGroupModel> groups = Collections.singletonList(group);
        List<RuleBasedPromotionModel> promotions = this.promotionSourceRuleDao.findPromotions(groups, "18",
                        Collections.emptySet());
        Assert.assertNotNull(promotions);
        Assert.assertEquals(0L, promotions.size());
        List<RuleBasedPromotionModel> promotionsForCategories = this.promotionSourceRuleDao.findPromotions(groups, "18",
                        (Set)ImmutableSet.of("585"));
        Assert.assertNotNull(promotionsForCategories);
        Assert.assertEquals(0L, promotionsForCategories.size());
    }


    @Test
    public void testFindPromotionSourceRulesOnlyCat()
    {
        PromotionGroupModel group = this.promotionDao.findPromotionGroupByCode("website1");
        Collection<PromotionGroupModel> groups = Arrays.asList(new PromotionGroupModel[] {group});
        ImmutableSet immutableSet = ImmutableSet.of("brand_5");
        List<RuleBasedPromotionModel> promotions = this.promotionSourceRuleDao.findPromotions(groups, "444444", (Set)immutableSet);
        Assert.assertNotNull(promotions);
        Assert.assertEquals(1L, promotions.size());
        Assert.assertEquals("promotion2", ((RuleBasedPromotionModel)promotions.get(0)).getCode());
    }


    @Test
    public void testFindPromotionSourceRulesOnlyCatWithExcludeStorefrontDisplay()
    {
        PromotionGroupModel group = this.promotionDao.findPromotionGroupByCode("website1");
        Collection<PromotionGroupModel> groups = Arrays.asList(new PromotionGroupModel[] {group});
        ImmutableSet immutableSet = ImmutableSet.of("brand_5");
        setExcludeFromStorefrontDisplayForRule("rule2");
        List<RuleBasedPromotionModel> promotions = this.promotionSourceRuleDao.findPromotions(groups, "444444", (Set)immutableSet);
        Assert.assertNotNull(promotions);
        Assert.assertEquals(0L, promotions.size());
    }


    @Test
    public void testFindPromotionSourceRulesNotFound()
    {
        PromotionGroupModel group = this.promotionDao.findPromotionGroupByCode("website1");
        Collection<PromotionGroupModel> groups = Arrays.asList(new PromotionGroupModel[] {group});
        ImmutableSet immutableSet = ImmutableSet.of("555");
        List<RuleBasedPromotionModel> promotions = this.promotionSourceRuleDao.findPromotions(groups, "555555", (Set)immutableSet);
        Assert.assertNotNull(promotions);
        Assert.assertEquals(0L, promotions.size());
    }


    @Test
    public void testFindPromotionSourceRulesOnlyProd()
    {
        PromotionGroupModel group = this.promotionDao.findPromotionGroupByCode("website1");
        Collection<PromotionGroupModel> groups = Arrays.asList(new PromotionGroupModel[] {group});
        List<RuleBasedPromotionModel> promotions = this.promotionSourceRuleDao.findPromotions(groups, "111111", null);
        Assert.assertNotNull(promotions);
        Assert.assertEquals(1L, promotions.size());
        Set<String> codes = (Set<String>)promotions.stream().map(p -> p.getCode()).collect(Collectors.toSet());
        Assert.assertTrue(codes.contains("promotion2"));
    }


    @Test
    public void testFindPromotionSourceRulesOnlyProdWithExcludeStorefrontDisplay()
    {
        PromotionGroupModel group = this.promotionDao.findPromotionGroupByCode("website1");
        Collection<PromotionGroupModel> groups = Arrays.asList(new PromotionGroupModel[] {group});
        setExcludeFromStorefrontDisplayForRule("rule2");
        setExcludeFromStorefrontDisplayForRule("rule3");
        List<RuleBasedPromotionModel> promotions = this.promotionSourceRuleDao.findPromotions(groups, "111111", null);
        Assert.assertNotNull(promotions);
        Assert.assertEquals(0L, promotions.size());
    }


    @Test
    public void testFindPromotionSourceRulesCombinedCats()
    {
        PromotionGroupModel group = this.promotionDao.findPromotionGroupByCode("website1");
        Collection<PromotionGroupModel> groups = Arrays.asList(new PromotionGroupModel[] {group});
        ImmutableSet immutableSet = ImmutableSet.of("cat10_1", "cat10_2");
        List<RuleBasedPromotionModel> promotions = this.promotionSourceRuleDao.findPromotions(groups, "prod10_1", (Set)immutableSet);
        Assert.assertNotNull(promotions);
        Assert.assertEquals(1L, promotions.size());
        Assert.assertEquals("promotion10", ((RuleBasedPromotionModel)promotions.get(0)).getCode());
    }


    @Test
    public void testFindPromotionSourceRulesCombinedCatsWithExcludeStorefrontDisplay()
    {
        setExcludeFromStorefrontDisplayForRule("rule10");
        PromotionGroupModel group = this.promotionDao.findPromotionGroupByCode("website1");
        Collection<PromotionGroupModel> groups = Arrays.asList(new PromotionGroupModel[] {group});
        ImmutableSet immutableSet = ImmutableSet.of("cat10_1", "cat10_2");
        List<RuleBasedPromotionModel> promotions = this.promotionSourceRuleDao.findPromotions(groups, "prod10_1", (Set)immutableSet);
        Assert.assertNotNull(promotions);
        Assert.assertEquals(0L, promotions.size());
    }


    @Test
    public void testFindPromotionSourceRulesCombinedCatsWithExcludedProduct()
    {
        PromotionGroupModel group = this.promotionDao.findPromotionGroupByCode("website1");
        Collection<PromotionGroupModel> groups = Arrays.asList(new PromotionGroupModel[] {group});
        ImmutableSet immutableSet = ImmutableSet.of("cat10_1", "cat10_2");
        List<RuleBasedPromotionModel> promotions = this.promotionSourceRuleDao.findPromotions(groups, "exclProd10_1", (Set)immutableSet);
        Assert.assertNotNull(promotions);
        Assert.assertEquals(0L, promotions.size());
    }


    @Test
    public void testFindPromotionSourceRulesCatsWithExcludedProduct()
    {
        PromotionGroupModel group = this.promotionDao.findPromotionGroupByCode("website1");
        Collection<PromotionGroupModel> groups = Arrays.asList(new PromotionGroupModel[] {group});
        ImmutableSet immutableSet = ImmutableSet.of("cat11_1");
        List<RuleBasedPromotionModel> promotions = this.promotionSourceRuleDao.findPromotions(groups, "exclProd11_1", (Set)immutableSet);
        Assert.assertNotNull(promotions);
        Assert.assertEquals(0L, promotions.size());
    }


    @Test
    public void testFindPromotionsWithExcludeStorefrontDisplayChangingFlag()
    {
        PromotionGroupModel group = this.promotionDao.findPromotionGroupByCode("website1");
        Collection<PromotionGroupModel> groups = Arrays.asList(new PromotionGroupModel[] {group});
        ImmutableSet immutableSet = ImmutableSet.of("cat12_1", "cat12_2");
        List<RuleBasedPromotionModel> promotionsBefore = this.promotionSourceRuleDao.findPromotions(groups, "anyProduct", (Set)immutableSet);
        Assert.assertNotNull(promotionsBefore);
        Assert.assertEquals(1L, promotionsBefore.size());
        Assert.assertEquals("promotion12", ((RuleBasedPromotionModel)promotionsBefore.get(0)).getCode());
        setExcludeFromStorefrontDisplayForRule("rule12");
        List<RuleBasedPromotionModel> promotionsAfter = this.promotionSourceRuleDao.findPromotions(groups, "anyProduct", (Set)immutableSet);
        Assert.assertNotNull(promotionsAfter);
        Assert.assertEquals(0L, promotionsAfter.size());
    }


    @Test
    public void testFindLastConditionIdForRuleAlreadyExists()
    {
        PromotionSourceRuleModel rule = (PromotionSourceRuleModel)this.ruleDao.findRuleByCode("rule6");
        Integer lastConditionId = this.promotionSourceRuleDao.findLastConditionIdForRule(rule);
        Assert.assertEquals(2L, lastConditionId.intValue());
    }


    @Test
    public void testFindLastConditionIdForRuleNotExists()
    {
        PromotionSourceRuleModel rule = (PromotionSourceRuleModel)this.ruleDao.findRuleByCode("rule7");
        Integer lastConditionId = this.promotionSourceRuleDao.findLastConditionIdForRule(rule);
        Assert.assertNull(lastConditionId);
    }


    @Test
    public void shouldFindAllExcludedCatForPromotionSourceRule()
    {
        PromotionSourceRuleModel rule = (PromotionSourceRuleModel)this.ruleDao.findRuleByCode("rule8");
        List<ExcludedCatForRuleModel> excludedCategories = this.promotionSourceRuleDao.findAllExcludedCatForPromotionSourceRule(rule, "primary-kie-base");
        Assertions.assertThat(excludedCategories.stream().map(ExcludedCatForRuleModel::getCategoryCode).toArray()).containsOnly(new Object[] {"cat1", "cat2"});
    }


    @Test
    public void shouldFindAllExcludedCatForPromotionSourceRuleForRuleAndModule()
    {
        PromotionSourceRuleModel rule = (PromotionSourceRuleModel)this.ruleDao.findRuleByCode("rule8");
        List<ExcludedCatForRuleModel> excludedCategories = this.promotionSourceRuleDao.findAllExcludedCatForPromotionSourceRule(rule, "primary-kie-base");
        Assertions.assertThat(excludedCategories.stream().map(ExcludedCatForRuleModel::getCategoryCode).toArray()).containsOnly(new Object[] {"cat1", "cat2"});
    }


    @Test
    public void shouldFindNoExcludedCatForPromotionSourceRuleForRuleAndModule()
    {
        PromotionSourceRuleModel rule = (PromotionSourceRuleModel)this.ruleDao.findRuleByCode("rule8");
        List<ExcludedCatForRuleModel> excludedCategories = this.promotionSourceRuleDao.findAllExcludedCatForPromotionSourceRule(rule, "secondary-kie-base");
        Assertions.assertThat(excludedCategories).isEmpty();
    }


    @Test
    public void shouldFindAllExcludedCatForPromotionSourceRuleNoExcluded()
    {
        PromotionSourceRuleModel rule = (PromotionSourceRuleModel)this.ruleDao.findRuleByCode("rule9");
        List<ExcludedCatForRuleModel> excludedCategories = this.promotionSourceRuleDao.findAllExcludedCatForPromotionSourceRule(rule, "primary-kie-base");
        Assertions.assertThat(excludedCategories).isEmpty();
    }


    @Test
    public void shouldFindAllExcludedCatForPromotionSourceRuleNoExcludedForRuleAndModule()
    {
        PromotionSourceRuleModel rule = (PromotionSourceRuleModel)this.ruleDao.findRuleByCode("rule9");
        List<ExcludedCatForRuleModel> excludedCategories = this.promotionSourceRuleDao.findAllExcludedCatForPromotionSourceRule(rule, "primary-kie-base");
        Assertions.assertThat(excludedCategories).isEmpty();
    }


    @Test
    public void testFindAllExcludedProductForPromotionSourceRule()
    {
        PromotionSourceRuleModel rule = (PromotionSourceRuleModel)this.ruleDao.findRuleByCode("rule8");
        List<ExcludedProductForRuleModel> excludedProducts = this.promotionSourceRuleDao.findAllExcludedProductForPromotionSourceRule(rule, "primary-kie-base");
        Assertions.assertThat(excludedProducts.stream().map(ExcludedProductForRuleModel::getProductCode).toArray())
                        .containsOnly(new Object[] {"prod1", "prod2"});
    }


    @Test
    public void shouldFindAllExcludedProductForPromotionSourceRuleForRuleAndModule()
    {
        PromotionSourceRuleModel rule = (PromotionSourceRuleModel)this.ruleDao.findRuleByCode("rule8");
        List<ExcludedProductForRuleModel> excludedProducts = this.promotionSourceRuleDao.findAllExcludedProductForPromotionSourceRule(rule, "primary-kie-base");
        Assertions.assertThat(excludedProducts.stream().map(ExcludedProductForRuleModel::getProductCode).toArray())
                        .containsOnly(new Object[] {"prod1", "prod2"});
    }


    @Test
    public void shouldFindAllExcludedProductForPromotionSourceRuleNoExcluded()
    {
        PromotionSourceRuleModel rule = (PromotionSourceRuleModel)this.ruleDao.findRuleByCode("rule9");
        List<ExcludedProductForRuleModel> excludedProducts = this.promotionSourceRuleDao.findAllExcludedProductForPromotionSourceRule(rule, "primary-kie-base");
        Assertions.assertThat(excludedProducts).isEmpty();
    }


    @Test
    public void shouldFindAllExcludedProductForPromotionSourceRuleNoExcludedForRuleAndModule()
    {
        PromotionSourceRuleModel rule = (PromotionSourceRuleModel)this.ruleDao.findRuleByCode("rule9");
        List<ExcludedProductForRuleModel> excludedProducts = this.promotionSourceRuleDao.findAllExcludedProductForPromotionSourceRule(rule, "primary-kie-base");
        Assertions.assertThat(excludedProducts).isEmpty();
    }


    @Test
    public void testFindPromotionSourceRulesCombinedCatsWithExpiredDates()
    {
        PromotionGroupModel group = this.promotionDao.findPromotionGroupByCode("website1");
        Collection<PromotionGroupModel> groups = Arrays.asList(new PromotionGroupModel[] {group});
        ImmutableSet immutableSet = ImmutableSet.of("cat19_1", "cat19_2");
        List<RuleBasedPromotionModel> promotions = this.promotionSourceRuleDao.findPromotions(groups, "prod19_1", (Set)immutableSet);
        Assert.assertNotNull(promotions);
        Assert.assertEquals(0L, promotions.size());
    }


    @Test
    public void shouldValidateEndDateApplicabilityForThePromotionSourceRulesConsideringQueryCaching()
    {
        PromotionGroupModel group = this.promotionDao.findPromotionGroupByCode("website1");
        Collection<PromotionGroupModel> groups = Collections.singletonList(group);
        Calendar calendar = Calendar.getInstance();
        calendar.set(2020, 11, 31, 20, 20, 59);
        List<RuleBasedPromotionModel> publishedPromotions = this.promotionSourceRuleDao.findPromotions(groups, "20",
                        Collections.emptySet(), calendar.getTime());
        Assert.assertNotNull(publishedPromotions);
        Assert.assertTrue(publishedPromotions.stream().anyMatch(p -> "promotion20".equals(p.getCode())));
        calendar.set(2020, 11, 31, 20, 21, 0);
        publishedPromotions = this.promotionSourceRuleDao.findPromotions(groups, "20",
                        Collections.emptySet(), calendar.getTime());
        Assert.assertNotNull(publishedPromotions);
        Assert.assertFalse(publishedPromotions.stream().anyMatch(p -> "promotion20".equals(p.getCode())));
        calendar.set(2020, 11, 31, 20, 21, 23);
        publishedPromotions = this.promotionSourceRuleDao.findPromotions(groups, "20",
                        Collections.emptySet(), calendar.getTime());
        Assert.assertNotNull(publishedPromotions);
        Assert.assertFalse(publishedPromotions.stream().anyMatch(p -> "promotion20".equals(p.getCode())));
        calendar.set(2020, 11, 31, 20, 22, 1);
        publishedPromotions = this.promotionSourceRuleDao.findPromotions(groups, "20", Collections.emptySet(), calendar.getTime());
        Assert.assertNotNull(publishedPromotions);
        Assert.assertFalse(publishedPromotions.stream().anyMatch(p -> "promotion20".equals(p.getCode())));
    }


    protected void setExcludeFromStorefrontDisplayForRule(String ruleCode)
    {
        PromotionSourceRuleModel rule = (PromotionSourceRuleModel)this.ruleDao.findRuleByCode(ruleCode);
        rule.setExcludeFromStorefrontDisplay(Boolean.TRUE);
        this.modelService.save(rule);
    }
}
