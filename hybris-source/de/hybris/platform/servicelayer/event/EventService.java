package de.hybris.platform.servicelayer.event;

import de.hybris.platform.servicelayer.event.events.AbstractEvent;
import java.util.Set;
import org.springframework.context.ApplicationListener;

public interface EventService
{
    void publishEvent(AbstractEvent paramAbstractEvent);


    boolean registerEventListener(ApplicationListener paramApplicationListener);


    boolean unregisterEventListener(ApplicationListener paramApplicationListener);


    Set<ApplicationListener> getEventListeners();
}
