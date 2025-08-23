package de.hybris.platform.servicelayer.event;

import de.hybris.platform.servicelayer.event.events.AbstractEvent;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;

public class ConfiguredTestListener extends AbstractEventListener<AbstractEvent>
{
    public AbstractEvent lastEvent;


    public AbstractEvent getLastEvent()
    {
        return this.lastEvent;
    }


    protected void onEvent(AbstractEvent event)
    {
        if(event instanceof MockEventServiceTest.CustomEvent || event instanceof MockEventServiceTest.CustomClusterEvent)
        {
            this.lastEvent = event;
        }
    }
}
