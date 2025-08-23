package de.hybris.platform.servicelayer.event.impl;

import de.hybris.platform.servicelayer.event.EventSender;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;

public class SpringEventSender implements EventSender, ApplicationContextAware
{
    protected ApplicationContext applicationContext;


    public void sendEvent(AbstractEvent event)
    {
        this.applicationContext.publishEvent((ApplicationEvent)event);
    }


    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {
        this.applicationContext = applicationContext;
    }
}
