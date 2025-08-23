package de.hybris.platform.promotionengineservices.validators;

import com.google.common.collect.ImmutableMap;
import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.promotionengineservices.model.RuleBasedPromotionModel;
import de.hybris.platform.promotionengineservices.validators.impl.DefaultRuleBasedPromotionsContextValidator;
import de.hybris.platform.ruleengine.dao.RuleEngineContextDao;
import de.hybris.platform.ruleengine.enums.RuleType;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import java.util.Map;
import javax.annotation.Resource;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

@IntegrationTest
public class DefaultRuleBasedPromotionsContextValidatorIT extends ServicelayerTest
{
    private static final String FIND_RULE_BASED_PROMOTION_BY_CODE = "SELECT {Pk} FROM {RuleBasedPromotion} WHERE {code} =?code";
    public static final String TEST_RULE_MAPPING_CATALOG = "testRuleMappingCatalog";
    @Resource(name = "droolsRuleBasedPromotionsContextValidator")
    private DefaultRuleBasedPromotionsContextValidator validator;
    @Resource
    private CatalogVersionService catalogVersionService;
    @Resource
    private RuleEngineContextDao ruleEngineContextDao;
    @Resource
    private FlexibleSearchService flexibleSearchService;
    @Resource
    private ModelService modelService;


    @Before
    public void setUp() throws Exception
    {
        importCsv("/promotionengineservices/test/droolsRulesMapping.impex", "UTF-8");
        assertDataPopulatedSuccessfully();
    }


    private RuleBasedPromotionModel findRuleBasedPromotion(String code)
    {
        ImmutableMap immutableMap = ImmutableMap.of("code", code);
        FlexibleSearchQuery query = new FlexibleSearchQuery("SELECT {Pk} FROM {RuleBasedPromotion} WHERE {code} =?code", (Map)immutableMap);
        return (RuleBasedPromotionModel)this.flexibleSearchService.searchUnique(query);
    }


    private void assertDataPopulatedSuccessfully()
    {
        try
        {
            this.ruleEngineContextDao.findRuleEngineContextByName("promotions-junit-context1");
            this.ruleEngineContextDao.findRuleEngineContextByName("promotions-junit-context2");
            this.ruleEngineContextDao.findRuleEngineContextByName("promotions-junit-context3");
            this.catalogVersionService.getCatalogVersion("testRuleMappingCatalog", "NoMappings");
            this.catalogVersionService.getCatalogVersion("testRuleMappingCatalog", "SingleMappings");
            this.catalogVersionService.getCatalogVersion("testRuleMappingCatalog", "DoubledMappings");
            this.modelService.detachAll();
        }
        catch(Exception ex)
        {
            Assertions.fail("Test data is missing");
        }
    }


    @Test
    public void shouldRejectPromoResultIfInvalidForGivenContext()
    {
        RuleBasedPromotionModel promotion = findRuleBasedPromotion("drools-rule1-junit");
        CatalogVersionModel catalogVersion = this.catalogVersionService.getCatalogVersion("testRuleMappingCatalog", "NoMappings");
        boolean applicable = this.validator.isApplicable(promotion, catalogVersion, RuleType.PROMOTION);
        Assertions.assertThat(applicable).isFalse();
    }


    @Test
    public void shouldAcceptPromoResultIfValidForGivenContext()
    {
        RuleBasedPromotionModel promotion = findRuleBasedPromotion("drools-rule3-junit");
        CatalogVersionModel catalogVersion = this.catalogVersionService.getCatalogVersion("testRuleMappingCatalog", "SingleMappings");
        boolean applicable = this.validator.isApplicable(promotion, catalogVersion, RuleType.PROMOTION);
        Assertions.assertThat(applicable).isTrue();
    }


    @Test
    public void shouldRejectPromoResultIfInvalidForGivenContexts()
    {
        RuleBasedPromotionModel promotion = findRuleBasedPromotion("drools-rule3-junit");
        CatalogVersionModel catalogVersion = this.catalogVersionService.getCatalogVersion("testRuleMappingCatalog", "DoubledMappings");
        boolean applicable = this.validator.isApplicable(promotion, catalogVersion, RuleType.PROMOTION);
        Assertions.assertThat(applicable).isFalse();
    }


    @Test
    public void shouldAcceptPromoResultIfValidForGivenContexts()
    {
        RuleBasedPromotionModel promotionResult1 = findRuleBasedPromotion("drools-rule1-junit");
        RuleBasedPromotionModel promotionResult2 = findRuleBasedPromotion("drools-rule2-junit");
        CatalogVersionModel catalogVersion = this.catalogVersionService.getCatalogVersion("testRuleMappingCatalog", "DoubledMappings");
        boolean applicable = (this.validator.isApplicable(promotionResult1, catalogVersion, RuleType.PROMOTION) && this.validator.isApplicable(promotionResult2, catalogVersion, RuleType.PROMOTION));
        Assertions.assertThat(applicable).isTrue();
    }
}
