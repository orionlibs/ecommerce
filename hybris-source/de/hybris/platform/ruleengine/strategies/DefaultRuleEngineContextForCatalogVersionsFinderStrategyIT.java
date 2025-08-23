package de.hybris.platform.ruleengine.strategies;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.ruleengine.dao.RuleEngineContextDao;
import de.hybris.platform.ruleengine.enums.RuleType;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineContextModel;
import de.hybris.platform.ruleengine.model.DroolsRuleEngineContextModel;
import de.hybris.platform.ruleengine.strategies.impl.DefaultRuleEngineContextForCatalogVersionsFinderStrategy;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.annotation.Resource;
import org.fest.assertions.Assertions;
import org.junit.Before;
import org.junit.Test;

@IntegrationTest
public class DefaultRuleEngineContextForCatalogVersionsFinderStrategyIT extends ServicelayerTest
{
    @Resource(name = "defaultRuleEngineContextForCatalogVersionsFinderStrategy")
    private DefaultRuleEngineContextForCatalogVersionsFinderStrategy strategy;
    @Resource
    private CatalogVersionService catalogVersionService;
    @Resource
    private RuleEngineContextDao ruleEngineContextDao;
    @Resource
    private ModelService modelService;
    private DroolsRuleEngineContextModel catmap01LiveContext;
    private DroolsRuleEngineContextModel catmap02LiveContext;
    private DroolsRuleEngineContextModel catmap01PreviewContext;
    private DroolsRuleEngineContextModel catmap02PreviewContext;


    @Before
    public void setUp() throws Exception
    {
        importCsv("/ruleengine/test/mappings/mappings-test-data.impex", "UTF-8");
        this
                        .catmap01LiveContext = (DroolsRuleEngineContextModel)this.ruleEngineContextDao.findRuleEngineContextByName("catmap01-live-context");
        this
                        .catmap02LiveContext = (DroolsRuleEngineContextModel)this.ruleEngineContextDao.findRuleEngineContextByName("catmap02-live-context");
        this
                        .catmap01PreviewContext = (DroolsRuleEngineContextModel)this.ruleEngineContextDao.findRuleEngineContextByName("catmap01-preview-context");
        this
                        .catmap02PreviewContext = (DroolsRuleEngineContextModel)this.ruleEngineContextDao.findRuleEngineContextByName("catmap01-preview-context");
        Assertions.assertThat(this.catmap01LiveContext).isNotNull();
        Assertions.assertThat(this.catmap02LiveContext).isNotNull();
        Assertions.assertThat(this.catmap01PreviewContext).isNotNull();
        Assertions.assertThat(this.catmap02PreviewContext).isNotNull();
        this.modelService.detachAll();
    }


    @Test
    public void testNullValues() throws Exception
    {
        List<AbstractRuleEngineContextModel> contexts = this.strategy.findRuleEngineContexts(null, null);
        Assertions.assertThat(contexts).isEmpty();
    }


    @Test
    public void testFindTwoValues() throws Exception
    {
        CatalogVersionModel staged01 = this.catalogVersionService.getCatalogVersion("catmap01", "Staged");
        CatalogVersionModel staged02 = this.catalogVersionService.getCatalogVersion("catmap02", "Staged");
        List<AbstractRuleEngineContextModel> contexts = this.strategy.findRuleEngineContexts(Arrays.asList(new CatalogVersionModel[] {staged01, staged02}, ), null);
        Assertions.assertThat(contexts).isNotEmpty();
        Assertions.assertThat(contexts).hasSize(2);
        contexts.forEach(c -> Assertions.assertThat(c).isInstanceOf(DroolsRuleEngineContextModel.class));
        Assertions.assertThat(contexts).contains(new Object[] {this.catmap01PreviewContext});
        Assertions.assertThat(contexts).contains(new Object[] {this.catmap02PreviewContext});
    }


    @Test
    public void testFindOneValue() throws Exception
    {
        CatalogVersionModel staged01 = this.catalogVersionService.getCatalogVersion("catmap01", "Staged");
        List<AbstractRuleEngineContextModel> contexts = this.strategy.findRuleEngineContexts(Collections.singletonList(staged01), null);
        Assertions.assertThat(contexts).isNotEmpty();
        Assertions.assertThat(contexts).hasSize(1);
        Assertions.assertThat(contexts).contains(new Object[] {this.catmap01PreviewContext});
    }


    @Test
    public void testFindOneValueWithMatchingRuleType() throws Exception
    {
        CatalogVersionModel staged01 = this.catalogVersionService.getCatalogVersion("catmap01", "Staged");
        List<AbstractRuleEngineContextModel> contexts = this.strategy.findRuleEngineContexts(Collections.singletonList(staged01), RuleType.DEFAULT);
        Assertions.assertThat(contexts).isNotEmpty();
        Assertions.assertThat(contexts).hasSize(1);
        Assertions.assertThat(contexts).contains(new Object[] {this.catmap01PreviewContext});
    }
}
