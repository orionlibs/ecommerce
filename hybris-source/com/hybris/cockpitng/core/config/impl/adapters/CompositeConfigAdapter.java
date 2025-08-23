/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl.adapters;

import com.hybris.cockpitng.core.config.CockpitConfigurationAdapter;
import com.hybris.cockpitng.core.config.CockpitConfigurationException;
import com.hybris.cockpitng.core.config.ConfigContext;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Required;

/**
 * The class allows to use more than one config adapter for one configuration.
 * It aggregates multiple adapters and apply them sequentially.
 */
public class CompositeConfigAdapter implements CockpitConfigurationAdapter
{
    private Class supportedType;
    private List<CockpitConfigurationAdapter> adapters;


    @Override
    public Class getSupportedType()
    {
        return supportedType;
    }


    @Override
    public Object adaptAfterLoad(final ConfigContext context, final Object object) throws CockpitConfigurationException
    {
        Object modifiedObject = object;
        for(final CockpitConfigurationAdapter adapter : adapters)
        {
            modifiedObject = adapter.adaptAfterLoad(context, object);
        }
        return modifiedObject;
    }


    @Override
    public Object adaptBeforeStore(final ConfigContext context, final Object object) throws CockpitConfigurationException
    {
        Object modifiedObject = object;
        for(final CockpitConfigurationAdapter adapter : adapters)
        {
            modifiedObject = adapter.adaptBeforeStore(context, object);
        }
        return modifiedObject;
    }


    @Required
    public void setSupportedType(final Class supportedType)
    {
        this.supportedType = supportedType;
    }


    @Required
    public void setAdapters(final List<CockpitConfigurationAdapter> adapters)
    {
        this.adapters = adapters;
    }


    @PostConstruct
    public void removeAdaptersWithIncorrectType()
    {
        adapters = adapters.stream()
                        .filter(a -> supportedType.isAssignableFrom(a.getSupportedType()))
                        .collect(Collectors.toList());
    }
}
