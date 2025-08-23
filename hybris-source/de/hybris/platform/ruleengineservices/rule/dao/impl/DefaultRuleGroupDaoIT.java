package de.hybris.platform.ruleengineservices.rule.dao.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.ruleengineservices.model.RuleGroupModel;
import de.hybris.platform.servicelayer.ServicelayerTest;
import java.util.List;
import javax.annotation.Resource;
import org.fest.assertions.Assertions;
import org.fest.assertions.ListAssert;
import org.junit.Before;
import org.junit.Test;

@IntegrationTest
public class DefaultRuleGroupDaoIT extends ServicelayerTest
{
    @Resource(name = "defaultRuleGroupDao")
    private DefaultRuleGroupDao ruleGroupDao;


    @Before
    public void setUp() throws Exception
    {
        importCsv("/ruleengineservices/test/rule/maintenance/test_source_rules.impex", "UTF-8");
    }


    @Test
    public void testFindAllReferredRuleGroups()
    {
        List<RuleGroupModel> ruleGroups = this.ruleGroupDao.findAllReferredRuleGroups();
        ((ListAssert)Assertions.assertThat(ruleGroups).isNotEmpty()).hasSize(1);
        Assertions.assertThat(ruleGroups.stream().anyMatch(rg -> "productPromotionRuleGroup".equals(rg.getCode()))).isTrue();
    }


    @Test
    public void testFindAllNotReferredRuleGroups()
    {
        List<RuleGroupModel> ruleGroups = this.ruleGroupDao.findAllNotReferredRuleGroups();
        ((ListAssert)Assertions.assertThat(ruleGroups).isNotEmpty()).hasSize(1);
        Assertions.assertThat(ruleGroups.stream().anyMatch(rg -> "emptyPromotionRuleGroup".equals(rg.getCode()))).isTrue();
    }
}
