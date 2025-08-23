/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.events;

/**
 *
 */
public interface CockpitEventPublisher
{
    void publishEvent(CockpitEvent event);


    void setQueueConnector(CockpitEventQueueConnector queueConnector);
}
