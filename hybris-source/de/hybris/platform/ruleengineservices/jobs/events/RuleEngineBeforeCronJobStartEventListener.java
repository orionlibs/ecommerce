package de.hybris.platform.ruleengineservices.jobs.events;

import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.event.events.BeforeCronJobStartEvent;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;
import de.hybris.platform.servicelayer.model.ModelService;
import org.springframework.beans.factory.annotation.Required;

public class RuleEngineBeforeCronJobStartEventListener extends AbstractEventListener<BeforeCronJobStartEvent>
{
    private ModelService modelService;


    protected void onEvent(BeforeCronJobStartEvent event)
    {
        if("RuleEngineJob".equals(event.getJobType()))
        {
            CronJobModel cronJob = (CronJobModel)getModelService().get(event.getCronJobPK());
            getModelService().removeAll(cronJob.getTriggers());
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
}
