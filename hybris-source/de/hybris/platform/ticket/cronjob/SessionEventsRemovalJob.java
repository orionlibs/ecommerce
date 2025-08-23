package de.hybris.platform.ticket.cronjob;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.ticket.enums.EventType;
import de.hybris.platform.ticket.event.dao.CustomerSupportEventDao;
import de.hybris.platform.ticketsystem.events.model.SessionEventModel;
import de.hybris.platform.ticketsystem.model.SessionEventsRemovalCronJobModel;
import de.hybris.platform.util.Config;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class SessionEventsRemovalJob extends AbstractJobPerformable<SessionEventsRemovalCronJobModel>
{
    private static final Logger LOG = LogManager.getLogger(SessionEventsRemovalJob.class);
    private CustomerSupportEventDao customerSupportEventDao;


    public PerformResult perform(SessionEventsRemovalCronJobModel arg0)
    {
        try
        {
            PageableData pagableData = new PageableData();
            pagableData.setPageSize(1000);
            LocalDateTime now = LocalDateTime.now();
            int eventsSurvivalAge = Config.getInt("ticketsystem.recent.sessions.survival.duration", 24);
            Date survivalDuration = Date.from(now.minusHours(eventsSurvivalAge).atZone(ZoneId.systemDefault()).toInstant());
            List<SessionEventModel> doomedEvents = getCustomerSupportEventDao().findAllEventsBeforeDate(EventType.EVENTS, survivalDuration);
            for(SessionEventModel doomedEvent : doomedEvents)
            {
                getModelService().remove(doomedEvent);
            }
            return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
        }
        catch(Exception e)
        {
            LOG.error("Exception occurred during events cleanup", e);
            return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
        }
    }


    protected CustomerSupportEventDao getCustomerSupportEventDao()
    {
        return this.customerSupportEventDao;
    }


    @Required
    public void setCustomerSupportEventDao(CustomerSupportEventDao customerSupportEventDao)
    {
        this.customerSupportEventDao = customerSupportEventDao;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }
}
