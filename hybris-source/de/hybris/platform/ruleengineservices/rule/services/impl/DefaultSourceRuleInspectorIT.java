package de.hybris.platform.ruleengineservices.rule.services.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.ruleengineservices.model.SourceRuleModel;
import de.hybris.platform.ruleengineservices.rule.dao.RuleDao;
import de.hybris.platform.ruleengineservices.rule.services.SourceRuleInspector;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import javax.annotation.Resource;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

@IntegrationTest
public class DefaultSourceRuleInspectorIT extends ServicelayerTransactionalTest
{
    public static final String PRODUCT_FIXED_PRICE_SOURCE_RULE = "product_fixed_price";
    @Resource
    private SourceRuleInspector sourceRuleInspector;
    @Resource
    private RuleDao ruleDao;


    @Before
    public void setUp() throws ImpExException
    {
        importCsv("/ruleengineservices/test/rule/defaultRuleConditionAndActionTest.impex", "utf-8");
    }


    @Test
    public void shouldConfirmThatConditionIsDefined() throws Exception
    {
        SourceRuleModel sourceRule = (SourceRuleModel)this.ruleDao.findRuleByCode("product_fixed_price");
        boolean result = this.sourceRuleInspector.hasRuleCondition(sourceRule, "y_group");
        Assertions.assertThat(result).isTrue();
    }


    @Test
    public void shouldConfirmThatConditionIsDefinedInConditionsTree() throws Exception
    {
        SourceRuleModel sourceRule = (SourceRuleModel)this.ruleDao.findRuleByCode("product_fixed_price");
        boolean result = this.sourceRuleInspector.hasRuleCondition(sourceRule, "y_qualifying_products");
        Assertions.assertThat(result).isTrue();
    }


    @Test
    public void shouldDenyThatConditionIsDefinedInConditionsTree() throws Exception
    {
        SourceRuleModel sourceRule = (SourceRuleModel)this.ruleDao.findRuleByCode("product_fixed_price");
        boolean result = this.sourceRuleInspector.hasRuleCondition(sourceRule, "y_undefined_condition");
        Assertions.assertThat(result).isFalse();
    }


    @Test
    public void shouldConfirmThatActionIsDefined() throws Exception
    {
        SourceRuleModel sourceRule = (SourceRuleModel)this.ruleDao.findRuleByCode("product_fixed_price");
        boolean result = this.sourceRuleInspector.hasRuleAction(sourceRule, "y_order_entry_fixed_price");
        Assertions.assertThat(result).isTrue();
    }


    @Test
    public void shouldDenyThatActionIsDefined() throws Exception
    {
        SourceRuleModel sourceRule = (SourceRuleModel)this.ruleDao.findRuleByCode("product_fixed_price");
        boolean result = this.sourceRuleInspector.hasRuleAction(sourceRule, "y_undefined_action");
        Assertions.assertThat(result).isFalse();
    }
}
