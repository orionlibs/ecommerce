package com.hybris.backoffice.events.processes;

import com.hybris.backoffice.events.DefaultBackofficeEventSender;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;

public class ProcessEventListener extends AbstractEventListener<AbstractProcessEvent>
{
    protected void onEvent(AbstractProcessEvent event)
    {
        getBackofficeEventSender().sendEvent((AbstractEvent)event);
    }


    public DefaultBackofficeEventSender getBackofficeEventSender()
    {
        throw new UnsupportedOperationException("Please define in the spring configuration a <lookup-method> for getBackofficeEventSender().");
    }
}
