package com.hybris.backoffice.events.sync;

import com.hybris.backoffice.events.DefaultBackofficeEventSender;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;

@Deprecated(since = "6.6", forRemoval = true)
public class SyncEventListener extends AbstractEventListener<AbstractSyncEvent>
{
    protected void onEvent(AbstractSyncEvent event)
    {
        getBackofficeEventSender().sendEvent((AbstractEvent)event);
    }


    public DefaultBackofficeEventSender getBackofficeEventSender()
    {
        throw new UnsupportedOperationException("Please define in the spring configuration a <lookup-method> for getBackofficeEventSender().");
    }
}
