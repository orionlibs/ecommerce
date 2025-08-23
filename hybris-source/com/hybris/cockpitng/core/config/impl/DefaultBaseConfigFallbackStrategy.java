/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl;

import com.hybris.cockpitng.core.config.ConfigContext;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.Base;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.Labels;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import java.util.Collections;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Strategy for creating fallback <p>base</p> configurations.
 * <p>
 * Creates a text representation of an object by concatenating all mandatory and unique fields
 * (see {@link com.hybris.cockpitng.dataaccess.facades.type.TypeFacade}) in that order.
 */
public class DefaultBaseConfigFallbackStrategy extends AbstractCockpitConfigurationFallbackStrategy<Base>
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultBaseConfigFallbackStrategy.class);
    private static final String SEPARATOR = " + ' - ' + ";


    @Override
    public Base loadFallbackConfiguration(final ConfigContext context, final Class<Base> configurationType)
    {
        final Base loadedConfig = new Base();
        final Labels labels = new Labels();
        loadedConfig.setLabels(labels);
        final String type = context.getAttribute(DefaultConfigContext.CONTEXT_TYPE);
        if(Double.class.getName().equals(type))
        {
            labels.setBeanId("javaLangDoubleLabelProvider");
        }
        else if(isException(type))
        {
            labels.setLabel(
                            "T(com.hybris.cockpitng.util.UITools).truncateText(!#empty(localizedMessage)?localizedMessage:#root, 200)");
        }
        else
        {
            final Set<String> attributes = getMandatoryUniqueAttributes(type);
            labels.setLabel(CollectionUtils.isNotEmpty(attributes) ? StringUtils.join(attributes, SEPARATOR) : "#root");
        }
        return loadedConfig;
    }


    protected Set<String> getMandatoryUniqueAttributes(final String typeCode)
    {
        if(StringUtils.isNotBlank(typeCode))
        {
            try
            {
                return getAttributes(typeCode, AbstractCockpitConfigurationFallbackStrategy.MANDATORY,
                                AbstractCockpitConfigurationFallbackStrategy.UNIQUE);
            }
            catch(final TypeNotFoundException e)
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug(String.format("Could not crate fallback BASE configuration for type [%s], because : %s", typeCode,
                                    e.getMessage()), e);
                }
            }
        }
        return Collections.emptySet();
    }


    protected boolean isException(final String className)
    {
        try
        {
            return className != null && Throwable.class.isAssignableFrom(Thread.currentThread().getContextClassLoader().loadClass(className));
        }
        catch(final ClassNotFoundException e)
        {
            LOG.debug("Given string does not denote a class: " + className, e);
            return false;
        }
    }
}
