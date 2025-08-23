package de.hybris.platform.apiregistryservices.dto;

import de.hybris.platform.apiregistryservices.model.events.EventConfigurationModel;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;
import java.io.Serializable;

public class EventSourceData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private AbstractEvent event;
    private EventConfigurationModel eventConfig;


    public void setEvent(AbstractEvent event)
    {
        this.event = event;
    }


    public AbstractEvent getEvent()
    {
        return this.event;
    }


    public void setEventConfig(EventConfigurationModel eventConfig)
    {
        this.eventConfig = eventConfig;
    }


    public EventConfigurationModel getEventConfig()
    {
        return this.eventConfig;
    }
}
