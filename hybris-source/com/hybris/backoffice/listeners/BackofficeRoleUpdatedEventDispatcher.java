package com.hybris.backoffice.listeners;

import com.hybris.backoffice.ApplicationUtils;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.tx.AfterSaveEvent;
import de.hybris.platform.tx.AfterSaveListener;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BackofficeRoleUpdatedEventDispatcher implements AfterSaveListener
{
    private static final Logger LOGGER = LoggerFactory.getLogger(BackofficeRoleUpdatedEventDispatcher.class);
    private static final int USER_GROUP_DEPLOYMENT_CODE = 5;
    private final ModelService modelService;
    private final EventService eventService;


    public BackofficeRoleUpdatedEventDispatcher(ModelService modelService, EventService eventService)
    {
        this.modelService = modelService;
        this.eventService = eventService;
    }


    public void afterSave(Collection<AfterSaveEvent> events)
    {
        if(shouldPerform())
        {
            Collection<AfterSaveEvent> roleUpdateEvent = getAfterSaveEventWithBackofficeRoleUpdate(events);
            if(CollectionUtils.isNotEmpty(roleUpdateEvent))
            {
                List<PK> roles = (List<PK>)roleUpdateEvent.stream().map(AfterSaveEvent::getPk).collect(Collectors.toList());
                getEventService().publishEvent((AbstractEvent)new BackofficeRoleUpdatedClusterAwareEvent(roles));
            }
        }
    }


    protected boolean shouldPerform()
    {
        return ApplicationUtils.isPlatformReady();
    }


    protected Collection<AfterSaveEvent> getAfterSaveEventWithBackofficeRoleUpdate(Collection<AfterSaveEvent> events)
    {
        return (Collection<AfterSaveEvent>)events.stream().filter(this::isBackofficeRoleChange).collect(Collectors.toList());
    }


    protected boolean isBackofficeRoleChange(AfterSaveEvent event)
    {
        try
        {
            if(5 == event.getPk().getTypeCode())
            {
                Object itemModel = getModelService().get(event.getPk());
                if(!(itemModel instanceof ItemModel))
                {
                    return false;
                }
                return ((ItemModel)itemModel).getItemtype().equals("BackofficeRole");
            }
            return false;
        }
        catch(RuntimeException e)
        {
            LOGGER.debug(e.getLocalizedMessage(), e);
            return false;
        }
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    protected EventService getEventService()
    {
        return this.eventService;
    }
}
