package com.hybris.backoffice.events;

import de.hybris.platform.servicelayer.event.EventSender;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;

@Deprecated(since = "6.7", forRemoval = true)
public class BackofficeEventListener extends AbstractEventListener<BackofficeEvent>
{
    protected void onEvent(BackofficeEvent event)
    {
        getBackofficeEventSender().sendEvent((AbstractEvent)event);
    }


    protected EventSender getBackofficeEventSender()
    {
        throw new UnsupportedOperationException("Please define in the spring configuration a <lookup-method> for getBackofficeEventSender().");
    }
}
