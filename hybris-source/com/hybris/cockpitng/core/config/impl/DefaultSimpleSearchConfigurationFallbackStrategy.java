/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl;

import com.hybris.cockpitng.core.config.CockpitConfigurationFallbackStrategy;
import com.hybris.cockpitng.core.config.ConfigContext;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.simplesearch.Field;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.simplesearch.SimpleSearch;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultSimpleSearchConfigurationFallbackStrategy implements CockpitConfigurationFallbackStrategy<SimpleSearch>
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultBaseConfigFallbackStrategy.class);
    private TypeFacade typeFacade;


    @Override
    public SimpleSearch loadFallbackConfiguration(final ConfigContext context, final Class<SimpleSearch> configurationType)
    {
        final DataType loadedType;
        final SimpleSearch search = new SimpleSearch();
        try
        {
            loadedType = typeFacade.load(context.getAttribute(DefaultConfigContext.CONTEXT_TYPE));
            if(loadedType == null)
            {
                return null;
            }
            for(final DataAttribute att : loadedType.getAttributes())
            {
                final DataType type = att.getValueType();
                if(att.isMandatory() && String.class.equals(type.getClazz()))
                {
                    final Field field = new Field();
                    field.setName(att.getQualifier());
                    search.getField().add(field);
                }
            }
        }
        catch(final TypeNotFoundException e)
        {
            LOG.error(e.getMessage(), e);
            return null;
        }
        return search;
    }


    @Required
    public void setTypeFacade(final TypeFacade typeFacade)
    {
        this.typeFacade = typeFacade;
    }
}
