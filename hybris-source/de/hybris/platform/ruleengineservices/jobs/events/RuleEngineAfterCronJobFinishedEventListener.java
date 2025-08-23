package de.hybris.platform.ruleengineservices.jobs.events;

import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.ruleengineservices.jobs.RuleEngineJobExecutionSynchronizer;
import de.hybris.platform.ruleengineservices.model.RuleEngineCronJobModel;
import de.hybris.platform.servicelayer.event.events.AfterCronJobFinishedEvent;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;
import de.hybris.platform.servicelayer.model.ModelService;
import org.springframework.beans.factory.annotation.Required;

public class RuleEngineAfterCronJobFinishedEventListener extends AbstractEventListener<AfterCronJobFinishedEvent>
{
    private ModelService modelService;
    private RuleEngineJobExecutionSynchronizer ruleEngineJobExecutionSynchronizer;


    protected void onEvent(AfterCronJobFinishedEvent event)
    {
        if("RuleEngineJob".equals(event.getJobType()))
        {
            CronJobModel cronJob = (CronJobModel)getModelService().get(event.getCronJobPK());
            getRuleEngineJobExecutionSynchronizer().releaseLock((RuleEngineCronJobModel)cronJob);
        }
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    protected RuleEngineJobExecutionSynchronizer getRuleEngineJobExecutionSynchronizer()
    {
        return this.ruleEngineJobExecutionSynchronizer;
    }


    @Required
    public void setRuleEngineJobExecutionSynchronizer(RuleEngineJobExecutionSynchronizer ruleEngineJobExecutionSynchronizer)
    {
        this.ruleEngineJobExecutionSynchronizer = ruleEngineJobExecutionSynchronizer;
    }
}
