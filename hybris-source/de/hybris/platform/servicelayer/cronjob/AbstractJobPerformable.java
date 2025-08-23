package de.hybris.platform.servicelayer.cronjob;

import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.session.SessionService;
import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractJobPerformable<T extends CronJobModel> implements JobPerformable<T>
{
    protected ModelService modelService;
    protected SessionService sessionService;
    protected FlexibleSearchService flexibleSearchService;


    public abstract PerformResult perform(T paramT);


    public boolean isAbortable()
    {
        return false;
    }


    public boolean isPerformable()
    {
        return true;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    @Required
    public void setSessionService(SessionService sessionService)
    {
        this.sessionService = sessionService;
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }


    protected final boolean clearAbortRequestedIfNeeded(T myCronJob)
    {
        this.modelService.refresh(myCronJob);
        if(BooleanUtils.isTrue(myCronJob.getRequestAbort()))
        {
            myCronJob.setRequestAbort(null);
            this.modelService.save(myCronJob);
            return true;
        }
        return false;
    }
}
