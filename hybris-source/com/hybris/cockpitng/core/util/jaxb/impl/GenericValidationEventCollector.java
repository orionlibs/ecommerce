/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.util.jaxb.impl;

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.util.ValidationEventCollector;

/**
 * Default framework implementation for schema validation purpose - used mainly for capture all schema violations during
 * marshaling/unmarshalling xml based configuration. Note: For details please look
 * {@link com.hybris.cockpitng.core.config.CockpitConfigurationService},
 * {@link com.hybris.cockpitng.core.persistence.WidgetPersistenceService}
 */
public class GenericValidationEventCollector extends ValidationEventCollector
{
    @Override
    public boolean handleEvent(final ValidationEvent event)
    {
        super.handleEvent(event);
        return true;
    }


    public String getSchemaValidationMessage()
    {
        final StringBuilder message = new StringBuilder();
        for(final ValidationEvent event : getEvents())
        {
            message.append(getSchemaValidationMessage(event));
        }
        return message.toString();
    }


    public static String getSchemaValidationMessage(final ValidationEvent event)
    {
        final StringBuilder message = new StringBuilder();
        message.append("SCHEMA VIOLATION: ").append(event.getLinkedException());
        message.append(System.lineSeparator());
        return message.toString();
    }
}
