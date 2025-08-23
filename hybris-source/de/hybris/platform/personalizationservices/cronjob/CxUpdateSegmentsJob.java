package de.hybris.platform.personalizationservices.cronjob;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.personalizationservices.CxUpdateSegmentContext;
import de.hybris.platform.personalizationservices.model.CxUpdateSegmentsCronJobModel;
import de.hybris.platform.personalizationservices.segment.CxSegmentService;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.site.BaseSiteService;
import java.util.Set;
import org.springframework.beans.factory.annotation.Required;

public class CxUpdateSegmentsJob extends AbstractJobPerformable<CxUpdateSegmentsCronJobModel>
{
    private CxSegmentService cxSegmentService;
    private BaseSiteService baseSiteService;


    public PerformResult perform(CxUpdateSegmentsCronJobModel job)
    {
        this.cxSegmentService.updateSegments(createUpdateSegmentContext(job));
        return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
    }


    protected CxUpdateSegmentContext createUpdateSegmentContext(CxUpdateSegmentsCronJobModel job)
    {
        CxUpdateSegmentContext context = new CxUpdateSegmentContext();
        context.setFullUpdate(job.isFullUpdate());
        if(job.isAllProviders())
        {
            context.setSegmentProviders(null);
        }
        else
        {
            context.setSegmentProviders(job.getProviders());
        }
        if(job.isAllBaseSites())
        {
            context.setBaseSites(Set.copyOf(this.baseSiteService.getAllBaseSites()));
        }
        else
        {
            context.setBaseSites(job.getBaseSites());
        }
        return context;
    }


    @Required
    protected CxSegmentService getCxSegmentService()
    {
        return this.cxSegmentService;
    }


    public void setCxSegmentService(CxSegmentService cxSegmentService)
    {
        this.cxSegmentService = cxSegmentService;
    }


    protected BaseSiteService getBaseSiteService()
    {
        return this.baseSiteService;
    }


    @Required
    public void setBaseSiteService(BaseSiteService baseSiteService)
    {
        this.baseSiteService = baseSiteService;
    }
}
