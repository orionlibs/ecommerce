/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.events.impl;

import com.hybris.cockpitng.core.events.CockpitEvent;
import com.hybris.cockpitng.core.events.CockpitEventPublisher;
import com.hybris.cockpitng.core.events.CockpitEventQueue;
import com.hybris.cockpitng.core.events.CockpitEventQueueConnector;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 *
 */
public class DefaultCockpitEventQueueConnector implements CockpitEventQueueConnector, ApplicationContextAware
{
    private ApplicationContext appCtx;
    private CockpitEventQueue eventQueue;


    @Override
    public void initialize()
    {
        for(final CockpitEventPublisher eventPublisher : getPublishers(appCtx))
        {
            eventPublisher.setQueueConnector(this);
        }
    }


    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException
    {
        this.appCtx = applicationContext;
    }


    @Override
    public void publishEvent(final CockpitEvent event)
    {
        eventQueue.publishEvent(event);
    }


    @Required
    public void setEventQueue(final CockpitEventQueue eventQueue)
    {
        this.eventQueue = eventQueue;
    }


    private List<CockpitEventPublisher> getPublishers(final ApplicationContext applicationContext)
    {
        final Map<String, CockpitEventPublisher> beansOfType = applicationContext.getBeansOfType(CockpitEventPublisher.class);
        final List<CockpitEventPublisher> ret = new ArrayList<CockpitEventPublisher>(beansOfType.values());
        if(applicationContext.getParent() != null)
        {
            ret.addAll(getPublishers(applicationContext.getParent()));
        }
        return ret;
    }
}
