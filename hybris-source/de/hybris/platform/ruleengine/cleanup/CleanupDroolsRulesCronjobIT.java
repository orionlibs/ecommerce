package de.hybris.platform.ruleengine.cleanup;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import javax.annotation.Resource;
import org.junit.Before;
import org.junit.Test;

@IntegrationTest
public class CleanupDroolsRulesCronjobIT extends ServicelayerTest
{
    @Resource
    private CronJobService cronJobService;


    @Before
    public void setUp() throws ImpExException
    {
        importCsv("/ruleengine/import/essentialdata-jobs.impex", "utf-8");
    }


    @Test
    public void shouldSuccessfullyExecuteDroolsRuleMaintenanceCronjob()
    {
        CronJobModel cronJob = this.cronJobService.getCronJob("droolsRulesMaintenanceCleanupJob");
        this.cronJobService.performCronJob(cronJob, true);
    }
}
