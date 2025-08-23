package de.hybris.platform.jalo.security.event;

import de.hybris.platform.servicelayer.event.events.AbstractEvent;

public abstract class AbstractLoginEvent extends AbstractEvent
{
    public AbstractLoginEvent(String uid)
    {
        super(uid);
    }


    public String getUid()
    {
        return (String)getSource();
    }
}
