package com.hybris.backoffice.events;

import de.hybris.platform.servicelayer.cronjob.CronJobHistoryInclude;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.event.events.AbstractCronJobPerformEvent;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.ConcurrentModificationException;
import java.util.Map;
import java.util.NoSuchElementException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractBackofficeCronJobEventListener<T extends AbstractCronJobPerformEvent> extends AbstractEventListener<T>
{
    private static final Logger lOGGER = LoggerFactory.getLogger(AbstractBackofficeCronJobEventListener.class);
    private EventService eventService;
    private TypeService typeService;
    private Map<String, CronJobHistoryInclude> processesIncludes;


    protected boolean isProcessUpdateEvent(T event)
    {
        Boolean isMatched = Boolean.valueOf(false);
        try
        {
            isMatched = Boolean.valueOf(this.processesIncludes.values().stream().anyMatch(include -> eventMatchesInclude((T)event, include)));
        }
        catch(ConcurrentModificationException ex1)
        {
            lOGGER.info(ex1.getMessage());
        }
        catch(NoSuchElementException ex2)
        {
            lOGGER.info(ex2.getMessage());
        }
        return isMatched.booleanValue();
    }


    protected boolean eventMatchesInclude(T event, CronJobHistoryInclude include)
    {
        return ((include.getJobCodes() != null && include.getJobCodes().contains(event.getJob())) ||
                        typesMatch(include.getJobTypeCode(), event.getJobType()) ||
                        typesMatch(include.getCronJobTypeCode(), event.getCronJobType()));
    }


    protected boolean typesMatch(String superType, String subType)
    {
        if(StringUtils.isEmpty(superType) || StringUtils.isEmpty(subType))
        {
            return false;
        }
        return this.typeService.isAssignableFrom(superType, subType);
    }


    public EventService getEventService()
    {
        return this.eventService;
    }


    @Required
    public void setEventService(EventService eventService)
    {
        this.eventService = eventService;
    }


    public TypeService getTypeService()
    {
        return this.typeService;
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    public Map<String, CronJobHistoryInclude> getProcessesIncludes()
    {
        return this.processesIncludes;
    }


    @Required
    public void setProcessesIncludes(Map<String, CronJobHistoryInclude> processesIncludes)
    {
        this.processesIncludes = processesIncludes;
    }
}
