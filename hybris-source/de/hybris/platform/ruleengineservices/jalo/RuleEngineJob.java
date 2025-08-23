package de.hybris.platform.ruleengineservices.jalo;

import de.hybris.platform.core.Registry;
import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.ruleengineservices.jobs.RuleEngineJobPerformable;
import de.hybris.platform.servicelayer.internal.jalo.ServicelayerJob;
import de.hybris.platform.servicelayer.model.ModelService;

public class RuleEngineJob extends ServicelayerJob
{
    protected boolean canPerform(CronJob cronJob)
    {
        CronJobModel model = getCronJob(cronJob);
        return (Registry.getApplicationContext().containsBean(getSpringId()) && getPerformable().isPerformable(model));
    }


    protected RuleEngineJobPerformable getPerformable()
    {
        return (RuleEngineJobPerformable)super.getPerformable();
    }


    protected CronJobModel getCronJob(CronJob cronJob)
    {
        return (CronJobModel)((ModelService)Registry.getApplicationContext().getBean("modelService", ModelService.class)).get(cronJob);
    }
}
