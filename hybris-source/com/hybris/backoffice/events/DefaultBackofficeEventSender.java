package com.hybris.backoffice.events;

import de.hybris.platform.servicelayer.event.EventSender;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultBackofficeEventSender implements EventSender
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultBackofficeEventSender.class);
    private EventSender backofficeEventsAdapter;


    public void sendEvent(AbstractEvent event)
    {
        if(this.backofficeEventsAdapter == null)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug(String.format("%s is not registered", new Object[] {EventSender.class.getName()}));
            }
            return;
        }
        this.backofficeEventsAdapter.sendEvent(event);
    }


    public void setBackofficeEventsAdapter(EventSender backofficeEventsAdapter)
    {
        this.backofficeEventsAdapter = backofficeEventsAdapter;
    }
}
