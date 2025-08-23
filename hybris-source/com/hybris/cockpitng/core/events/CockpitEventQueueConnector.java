/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.events;

/**
 *
 */
public interface CockpitEventQueueConnector
{
    void initialize();


    void publishEvent(CockpitEvent event);
}
