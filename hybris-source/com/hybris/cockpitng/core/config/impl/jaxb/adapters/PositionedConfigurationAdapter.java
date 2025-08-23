/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl.jaxb.adapters;

import com.hybris.cockpitng.core.config.CockpitConfigurationAdapter;
import com.hybris.cockpitng.core.config.CockpitConfigurationException;
import com.hybris.cockpitng.core.config.ConfigContext;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.commonconfig.Positioned;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A configuration adapter that sort a list of {@link Positioned} elements available as specified property of
 * configuration
 */
public class PositionedConfigurationAdapter<CONFIG, POSITIONED extends Positioned> implements CockpitConfigurationAdapter<CONFIG>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(PositionedConfigurationAdapter.class);
    private final PositionedSort<POSITIONED> positionedSort;
    private final Class<CONFIG> supportedType;
    private final String property;


    public PositionedConfigurationAdapter(final PositionedSort<POSITIONED> positionedSort, final Class<CONFIG> supportedType)
    {
        this(positionedSort, supportedType, null);
    }


    public PositionedConfigurationAdapter(final PositionedSort<POSITIONED> positionedSort, final Class<CONFIG> supportedType,
                    final String property)
    {
        this.positionedSort = positionedSort;
        this.supportedType = supportedType;
        this.property = property;
    }


    @Override
    public Class<CONFIG> getSupportedType()
    {
        return supportedType;
    }


    protected String getProperty()
    {
        return property;
    }


    protected PositionedSort<POSITIONED> getPositionedSort()
    {
        return positionedSort;
    }


    @Override
    public CONFIG adaptBeforeMerge(final ConfigContext context, final CONFIG config) throws CockpitConfigurationException
    {
        return config;
    }


    @Override
    public CONFIG adaptAfterLoad(final ConfigContext context, final CONFIG config) throws CockpitConfigurationException
    {
        final List<POSITIONED> positioned = getPositionedList(context, config);
        if(positioned != null)
        {
            getPositionedSort().sort(positioned);
        }
        return config;
    }


    protected List<POSITIONED> getPositionedList(final ConfigContext context, final CONFIG config)
    {
        if(config != null && getProperty() != null)
        {
            try
            {
                final Object propertyValue = PropertyUtils.getProperty(config, getProperty());
                return getListPropertyValue(propertyValue);
            }
            catch(final IllegalAccessException | InvocationTargetException | NoSuchMethodException e)
            {
                LOGGER.error(e.getLocalizedMessage(), e);
                return null;
            }
        }
        else
        {
            return getListPropertyValue(config);
        }
    }


    protected List<POSITIONED> getListPropertyValue(final Object propertyValue)
    {
        if(propertyValue != null && !(propertyValue instanceof List))
        {
            LOGGER.error("Cannot sort a property of type: " + propertyValue.getClass(), new ClassCastException());
            return null;
        }
        else
        {
            return (List<POSITIONED>)propertyValue;
        }
    }


    @Override
    public CONFIG adaptBeforeStore(final ConfigContext context, final CONFIG config) throws CockpitConfigurationException
    {
        return config;
    }
}
