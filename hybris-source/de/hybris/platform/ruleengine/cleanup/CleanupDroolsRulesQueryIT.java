package de.hybris.platform.ruleengine.cleanup;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.ruleengine.cronjob.CleanupDroolsRulesStrategy;
import de.hybris.platform.ruleengine.model.DroolsRuleModel;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.internal.model.MaintenanceCleanupJobModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.List;
import javax.annotation.Resource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

@IntegrationTest
public class CleanupDroolsRulesQueryIT extends ServicelayerTest
{
    @Resource(name = "flexibleSearchService")
    private FlexibleSearchService flexibleSearchService;
    @Resource
    private CleanupDroolsRulesStrategy cleanupDroolsRulesStrategy;


    @Before
    public void setUp() throws ImpExException
    {
        importCsv("/ruleengine/test/cronjob/cleanUpdroolsrule.impex", "utf-8");
    }


    @Test
    public void testQueryOfSelectDroolsRulesTobeRemoved()
    {
        CronJobModel cjm = new CronJobModel();
        MaintenanceCleanupJobModel cleanupJob = new MaintenanceCleanupJobModel();
        cjm.setJob((JobModel)cleanupJob);
        SearchResult<DroolsRuleModel> search = this.flexibleSearchService.search(this.cleanupDroolsRulesStrategy
                        .createFetchQuery(cjm));
        List<DroolsRuleModel> list = search.getResult();
        Assert.assertEquals(3L, list.size());
    }
}
