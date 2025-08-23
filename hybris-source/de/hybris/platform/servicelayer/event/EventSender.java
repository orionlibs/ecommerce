package de.hybris.platform.servicelayer.event;

import de.hybris.platform.servicelayer.event.events.AbstractEvent;

public interface EventSender
{
    void sendEvent(AbstractEvent paramAbstractEvent);
}
