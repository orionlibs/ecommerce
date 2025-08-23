package de.hybris.platform.ruleengine.dao.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineContextModel;
import de.hybris.platform.servicelayer.ServicelayerTest;
import java.util.List;
import javax.annotation.Resource;
import org.fest.assertions.Assertions;
import org.fest.assertions.ListAssert;
import org.junit.Before;
import org.junit.Test;

@IntegrationTest
public class DefaultRuleEngineContextDaoIT extends ServicelayerTest
{
    @Resource(name = "defaultRulesModuleDao")
    private DefaultRulesModuleDao rulesModuleDao;
    @Resource(name = "defaultRuleEngineContextDao")
    private DefaultRuleEngineContextDao ruleEngineContextDao;


    @Before
    public void setUp() throws Exception
    {
        importCsv("/ruleengine/test/dao/rulesmoduledao-test-data.impex", "UTF-8");
    }


    @Test
    public void testFindRuleEngineContextByRulesModule()
    {
        List<? extends AbstractRuleEngineContextModel> ctxs = this.ruleEngineContextDao.findRuleEngineContextByRulesModule(this.rulesModuleDao.findByName("live-module"));
        ((ListAssert)Assertions.assertThat(ctxs).isNotEmpty()).hasSize(1);
        Assertions.assertThat(ctxs.stream().anyMatch(ctx -> "live-context".equals(ctx.getName()))).isTrue();
    }


    @Test
    public void testFindRuleEngineContextByRulesModuleNotFound()
    {
        List<? extends AbstractRuleEngineContextModel> ctxs = this.ruleEngineContextDao.findRuleEngineContextByRulesModule(this.rulesModuleDao.findByName("preview-module"));
        Assertions.assertThat(ctxs).isEmpty();
    }
}
